package com.simiacryptus.openai

import com.google.common.util.concurrent.*
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.io.BufferedOutputStream
import java.io.IOException
import java.time.Duration
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.HashSet
import kotlin.math.pow

@Suppress("MemberVisibilityCanBePrivate")
open class HttpClientManager(
    protected val logLevel: Level = Level.INFO,
    val auxillaryLogOutputStream: BufferedOutputStream? = null
) {

    companion object {
        val log = LoggerFactory.getLogger(HttpClientManager::class.java)
        val scheduledPool: ListeningScheduledExecutorService =
            MoreExecutors.listeningDecorator(
                ScheduledThreadPoolExecutor(
                    4,
                    ThreadFactoryBuilder().setNameFormat("API Scheduler %d").build()
                )
            )
        val workPool: ListeningExecutorService =
            MoreExecutors.listeningDecorator(
                ThreadPoolExecutor(
                    8,
                    16,
                    0,
                    TimeUnit.MILLISECONDS,
                    LinkedBlockingQueue(),
                    ThreadFactoryBuilder().setNameFormat("API Thread %d").build()
                )
            )
        val startTime by lazy { System.currentTimeMillis() }

    }

    fun <T> withPool(fn: () -> T): T = workPool.submit(Callable {
        return@Callable fn()
    }).get()

    fun <T> withExpBackoffRetry(retryCount: Int = 7, sleepScale: Long = TimeUnit.SECONDS.toMillis(5), fn: () -> T): T {
        var lastException: Exception? = null
        var i = 0
        while (i++ < retryCount) {
            try {
                return fn()
            } catch (e: ModelMaxException) {
                throw e
            } catch (e: RateLimitException) {
                i--;
                this.log(Level.DEBUG, "Rate limited; retrying ($i/$retryCount): " + e.message)
                Thread.sleep(e.delay)
            } catch (e: Exception) {
                val modelMaxException = modelMaxException(e)
                if (null != modelMaxException) throw modelMaxException
                val rateLimitException = rateLimitException(e)
                if (null != rateLimitException) throw rateLimitException
                val apiKeyException = apiKeyException(e)
                if (null != apiKeyException) throw apiKeyException
                lastException = e
                this.log(Level.DEBUG, "Request failed; retrying ($i/$retryCount): " + e.message)
                Thread.sleep(sleepScale * 2.0.pow(i.toDouble()).toLong())
            }
        }
        throw lastException!!
    }

    private fun modelMaxException(e: Throwable?): ModelMaxException? {
        if (e == null) return null
        if (e is ModelMaxException) return e
        if (e.cause != null && e.cause != e) return modelMaxException(e.cause)
        return null
    }

    private fun rateLimitException(e: Throwable?): RateLimitException? {
        if (e == null) return null
        if (e is RateLimitException) return e
        if (e.cause != null && e.cause != e) return rateLimitException(e.cause)
        return null
    }

    private fun apiKeyException(e: Throwable?): IOException? {
        if (e == null) return null
        if (e is IOException && true == e.message?.contains("Incorrect API key")) return e
        if (e.cause != null && e.cause != e) return apiKeyException(e.cause)
        return null
    }

    protected val clients: MutableMap<Thread, CloseableHttpClient> = WeakHashMap()
    fun getClient(thread: Thread = Thread.currentThread()): CloseableHttpClient =
        if (thread in clients) clients[thread]!!
        else synchronized(clients) {
            val client = HttpClientBuilder.create().build()
            clients[thread] = client
            client
        }

    protected fun closeClient(thread: Thread) {
        try {
            synchronized(clients) {
                clients[thread]
            }?.close()
            thread.interrupt()
        } catch (e: IOException) {
            log(Level.DEBUG, "Error closing client: " + e.message)
        }
    }

    protected fun <T> withCancellationMonitor(fn: () -> T): T {
        val currentThread = Thread.currentThread()
        return withCancellationMonitor(fn) { currentThread.isInterrupted }
    }

    protected fun <T> withCancellationMonitor(fn: () -> T, cancelCheck: () -> Boolean): T {
        val threads = HashSet<Thread>()
        threads.add(Thread.currentThread())
        val isCompleted = AtomicBoolean(false)
        val start = Date()
        val cancellationFuture = scheduledPool.scheduleAtFixedRate({
            if (cancelCheck()) {
                log(Level.DEBUG, "Request cancelled at ${Date()} (started $start); closing client for thread $threads")
                threads.forEach { closeClient(it) }
            }
        }, 0, 10, TimeUnit.MILLISECONDS)
        try {
            return runAsync(threads, fn)
        } finally {
            cancellationFuture.cancel(false)
        }
    }

    protected fun <T> withTimeout(duration: Duration, fn: () -> T): T {
        val threads = HashSet<Thread>()
        val currentThread = Thread.currentThread()
        threads.add(currentThread)
        val isTimeout = AtomicBoolean(false)
        val start = Date()
        val cancellationFuture = scheduledPool.schedule({
            log(
                Level.DEBUG,
                "Request timed out after $duration at ${Date()} (started $start); closing client for thread $threads"
            )
            isTimeout.set(true)
            threads.forEach { closeClient(it) }
        }, duration.toMillis(), TimeUnit.MILLISECONDS)
        try {
            return fn()
        } catch (ex: InterruptedException) {
            if (!isTimeout.get()) throw ex
            throw RuntimeException(ex)
        } finally {
            threads.remove(currentThread)
            cancellationFuture.cancel(false)
        }
    }


    private fun <T> runAsync(
        threads: MutableSet<Thread>,
        fn: () -> T
    ): T {
        val isDone = Semaphore(0)
        log.info("Async request started")
        val future = workPool.submit(Callable<T> {
            val currentThread = Thread.currentThread()
            try {
                threads.add(currentThread)
                log.info("Async request started; running $fn")
                fn()
            } finally {
                threads.remove(currentThread)
                isDone.release()
                log.info("Async request completed; isDone ${System.identityHashCode(isDone)} released")
            }
        })
        try {
            isDone.acquire()
            log.info("Async request completed; getting value")
            val get = future.get()
            log.info("Async request completed; got value")
            return get
        } finally {
            log.info("Async request completed")
        }
    }

    protected fun <T> withReliability(requestTimeoutSeconds: Long = (5 * 60), retryCount: Int = 3, fn: () -> T): T =
        withExpBackoffRetry(retryCount) {
            withTimeout(Duration.ofSeconds(requestTimeoutSeconds)) {
                withCancellationMonitor(fn)
            }
        }

    protected fun <T> withPerformanceLogging(fn: () -> T): T {
        val start = Date()
        try {
            return fn()
        } finally {
            log(Level.DEBUG, "Request completed in ${Date().time - start.time}ms")
        }
    }

    protected fun <T> withClient(fn: java.util.function.Function<CloseableHttpClient, T>): T {
        val client = getClient()
        try {
            synchronized(clients) {
                clients[Thread.currentThread()] = client
            }
            client.use { httpClient ->
                return fn.apply(httpClient)
            }
        } finally {
            synchronized(clients) {
                clients.remove(Thread.currentThread())
            }
        }
    }

    protected open fun log(level: Level = logLevel, msg: String) {
        val message = msg.trim().replace("\n", "\n\t")
        when (level) {
            Level.ERROR -> log.error(message)
            Level.WARN -> log.warn(message)
            Level.INFO -> log.info(message)
            Level.DEBUG -> log.debug(message)
            Level.TRACE -> log.debug(message)
            else -> log.debug(message)
        }
        if (auxillaryLogOutputStream != null) {
            auxillaryLogOutputStream?.write(
                "[$level] [${"%.3f".format((System.currentTimeMillis() - startTime) / 1000.0)}] ${
                    message.replace(
                        "\n",
                        "\n\t"
                    )
                }\n".toByteArray()
            )
            auxillaryLogOutputStream?.flush()
        }
    }

}