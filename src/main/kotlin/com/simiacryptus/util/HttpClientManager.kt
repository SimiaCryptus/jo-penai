package com.simiacryptus.util

import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.ListeningScheduledExecutorService
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.simiacryptus.openai.exceptions.InvalidModelException
import com.simiacryptus.openai.exceptions.ModelMaxException
import com.simiacryptus.openai.exceptions.QuotaException
import com.simiacryptus.openai.exceptions.RateLimitException
import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.util.Timeout
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.io.BufferedOutputStream
import java.io.IOException
import java.time.Duration
import java.util.*
import java.util.concurrent.*
import java.util.function.Function
import kotlin.math.pow

open class HttpClientManager(
    private val logLevel: Level = Level.INFO,
    private val logStreams: MutableList<BufferedOutputStream> = mutableListOf()
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

        fun modelMaxException(e: Throwable?): ModelMaxException? {
            if (e == null) return null
            if (e is ModelMaxException) return e
            if (e.cause != null && e.cause != e) return modelMaxException(e.cause)
            return null
        }

        fun rateLimitException(e: Throwable?): RateLimitException? {
            if (e == null) return null
            if (e is RateLimitException) return e
            if (e.cause != null && e.cause != e) return rateLimitException(e.cause)
            return null
        }

        fun quotaLimitException(e: Throwable?): QuotaException? {
            if (e == null) return null
            if (e is QuotaException) return e
            if (e.cause != null && e.cause != e) return quotaLimitException(e.cause)
            return null
        }

        fun invalidModelException(e: Throwable?): InvalidModelException? {
            if (e == null) return null
            if (e is InvalidModelException) return e
            if (e.cause != null && e.cause != e) return invalidModelException(e.cause)
            return null
        }

        fun apiKeyException(e: Throwable?): IOException? {
            if (e == null) return null
            if (e is IOException && true == e.message?.contains("Incorrect API key")) return e
            if (e.cause != null && e.cause != e) return apiKeyException(e.cause)
            return null
        }

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
                i--
                this.log(Level.DEBUG, "Rate limited; retrying ($i/$retryCount): " + e.message)
                Thread.sleep(e.delay)
            } catch (e: Exception) {
                onException(e)
                lastException = e
                this.log(Level.DEBUG, "Request failed; retrying ($i/$retryCount): " + e.message)
                Thread.sleep(sleepScale * 2.0.pow(i.toDouble()).toLong())
            }
        }
        throw lastException!!
    }

    protected open fun onException(e: Exception) {
        val modelMaxException = modelMaxException(e)
        if (null != modelMaxException) throw modelMaxException
        val rateLimitException = rateLimitException(e)
        if (null != rateLimitException) throw rateLimitException
        val apiKeyException = apiKeyException(e)
        if (null != apiKeyException) throw apiKeyException
        val quotaException = quotaLimitException(e)
        if (null != quotaException) throw quotaException
        val invalidModelException = invalidModelException(e)
        if (null != invalidModelException) throw invalidModelException
    }

    open val client: CloseableHttpClient by lazy {
        HttpClientBuilder.create()
        .setDefaultRequestConfig(
            RequestConfig.custom()
                .setResponseTimeout(Timeout.ofSeconds(0))
                .setConnectionRequestTimeout(Timeout.ofSeconds(0))
                .build()
        )
        .setRetryStrategy(DefaultHttpRequestRetryStrategy(0, Timeout.ofSeconds(1)))
        .setConnectionManager(with(PoolingHttpClientConnectionManager()) {
            setDefaultConnectionConfig(
                ConnectionConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(30))
                    .build()
            )
            defaultMaxPerRoute = 10
            maxTotal = 100
            this
        })
        .build()
    }

    fun <T> withCancellationMonitor(fn: () -> T, cancelCheck: () -> Boolean): T {
        var thread = Thread.currentThread()
        val start = Date()
        log(Level.DEBUG, "Request started at ${Date()}; monitoring for cancellation for thread $thread")
        val cancellationFuture = scheduledPool.scheduleAtFixedRate({
            if (cancelCheck()) {
                log(Level.DEBUG, "Request cancelled at ${Date()} (started $start); closing client for thread $thread")
                thread.interrupt()
            }
        }, 0, 100, TimeUnit.MILLISECONDS)
        try {
            return withPool { fn() }
        } finally {
            cancellationFuture.cancel(false)
        }
    }

    fun <T> withTimeout(duration: Duration, fn: () -> T): T {
        var thread = Thread.currentThread()
        val start = Date()
        val cancellationFuture = scheduledPool.schedule({
            log(
                Level.DEBUG,
                "Request timed out after $duration at ${Date()} (started $start); closing client for thread $thread"
            )
            thread.interrupt()
        }, duration.toMillis(), TimeUnit.MILLISECONDS)
        try {
            return withPool { fn() }
        } finally {
            cancellationFuture.cancel(false)
        }
    }

    fun <T> withReliability(requestTimeoutSeconds: Long = (5 * 60), retryCount: Int = 3, fn: () -> T): T =
        withExpBackoffRetry(retryCount) { withTimeout(Duration.ofSeconds(requestTimeoutSeconds), fn) }

    fun <T> withPerformanceLogging(fn: () -> T): T {
        val start = Date()
        try {
            return fn()
        } finally {
            log(Level.DEBUG, "Request completed in ${Date().time - start.time}ms")
        }
    }

    fun <T> withClient(fn: Function<CloseableHttpClient, T>): T {
        Thread.currentThread()
        return fn.apply(client)
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
        logStreams.forEach { auxillaryLogOutputStream ->
            auxillaryLogOutputStream.write(
                "[$level] [${"%.3f".format((System.currentTimeMillis() - startTime) / 1000.0)}] ${
                    message.replace(
                        "\n",
                        "\n\t"
                    )
                }\n".toByteArray()
            )
            auxillaryLogOutputStream.flush()
        }
    }

}