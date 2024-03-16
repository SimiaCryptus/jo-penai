package com.simiacryptus.jopenai

import com.google.common.util.concurrent.ListeningScheduledExecutorService
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.simiacryptus.jopenai.exceptions.InvalidModelException
import com.simiacryptus.jopenai.exceptions.ModelMaxException
import com.simiacryptus.jopenai.exceptions.QuotaException
import com.simiacryptus.jopenai.exceptions.RateLimitException
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
  val logStreams: MutableList<BufferedOutputStream> = mutableListOf(),
  private val scheduledPool: ListeningScheduledExecutorService = Companion.scheduledPool,
  private val workPool: ThreadPoolExecutor = Companion.workPool,
  private val client: CloseableHttpClient = Companion.client
) : API() {

  companion object {

    val scheduledPool: ListeningScheduledExecutorService =
      MoreExecutors.listeningDecorator(
        ScheduledThreadPoolExecutor(
          4,
          ThreadFactoryBuilder().setNameFormat("API Scheduler %d").build()
        )
      )

    val workPool: ThreadPoolExecutor =
      ThreadPoolExecutor(
        8,
        16,
        0,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(),
        ThreadFactoryBuilder().setNameFormat("API Thread %d").build()
      )

    val client: CloseableHttpClient = HttpClientBuilder.create()
      .setRetryStrategy(DefaultHttpRequestRetryStrategy(0, Timeout.ofSeconds(1)))
      .setConnectionManager(with(PoolingHttpClientConnectionManager()) {
        defaultMaxPerRoute = 100
        maxTotal = 100
        this
      }).build()

    private val log = LoggerFactory.getLogger(HttpClientManager::class.java)
    val startTime by lazy { System.currentTimeMillis() }

    fun modelMaxException(e: Throwable?): ModelMaxException? = when {
      e == null -> null
      e is ModelMaxException -> e
      e.cause != null && e.cause != e -> modelMaxException(e.cause)
      else -> null
    }

    fun rateLimitException(e: Throwable?): RateLimitException? = when {
      e == null -> null
      e is RateLimitException -> e
      e.cause != null && e.cause != e -> rateLimitException(e.cause)
      else -> null
    }

    fun quotaLimitException(e: Throwable?): QuotaException? = when {
      e == null -> null
      e is QuotaException -> e
      e.cause != null && e.cause != e -> quotaLimitException(e.cause)
      else -> null
    }

    fun invalidModelException(e: Throwable?): InvalidModelException? = when {
      e == null -> null
      e is InvalidModelException -> e
      e.cause != null && e.cause != e -> invalidModelException(e.cause)
      else -> null
    }

    fun apiKeyException(e: Throwable?): IOException? = when {
      e == null -> null
      e is IOException && true == e.message?.contains("Incorrect API key") -> e
      e.cause != null && e.cause != e -> apiKeyException(e.cause)
      else -> null
    }

  }

  /**
   * The HTTP client will not always respond to interrupts,
   * So we need to isolate HTTP calls into a separate thread
   * The caller will then return when interrupted
   */
  private fun <T> withPool(fn: () -> T): T {
    val future = workPool.submit(Callable {
      return@Callable fn()
    })
    try {
      return future.get()
    } catch (e: InterruptedException) {
      future.cancel(true)
      throw e
    } catch (e: ExecutionException) {
      future.cancel(true)
      throw e
    } catch (e: CancellationException) {
      future.cancel(true)
      throw e
    } catch (e: TimeoutException) {
      future.cancel(true)
      throw e
    } catch (e: Exception) {
      future.cancel(true)
      throw e
    }
  }

  private fun <T> withExpBackoffRetry(
    retryCount: Int,
    sleepScale: Long = TimeUnit.SECONDS.toMillis(5),
    fn: () -> T
  ): T {
    var lastException: Exception? = null
    var i = 0
    while (i++ < retryCount) {
      val sleepPeriod = sleepScale * 2.0.pow(i.toDouble()).toLong()
      try {
        return fn()
      } catch (e: Throwable) {
        when(val exception = unwrapException(e)) {
          is ModelMaxException -> throw exception
          is RateLimitException -> {
            lastException = exception
            i--
            this.log(Level.DEBUG, "Rate limited; retrying ($i/$retryCount): " + exception.message)
            Thread.sleep((TimeUnit.SECONDS.toMillis(exception.delay)).coerceAtLeast(sleepPeriod))
          }
          is Exception -> {
            lastException = exception
            this.log(Level.DEBUG, "Request failed; retrying ($i/$retryCount): " + exception.message)
            Thread.sleep(sleepPeriod)
          }
          else -> throw exception
        }
      }
    }
    throw lastException!!
  }

  protected open fun unwrapException(e: Throwable) : Throwable {
    val modelMaxException = modelMaxException(e)
    if (null != modelMaxException) return modelMaxException
    val rateLimitException = rateLimitException(e)
    if (null != rateLimitException) return rateLimitException
    val apiKeyException = apiKeyException(e)
    if (null != apiKeyException) return apiKeyException
    val quotaException = quotaLimitException(e)
    if (null != quotaException) return quotaException
    val invalidModelException = invalidModelException(e)
    if (null != invalidModelException) return invalidModelException
    return e
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

  private fun <T> withTimeout(duration: Duration, fn: () -> T): T {
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
    logStreams.forEach { stream ->
      stream.write(
        "[$level] [${"%.3f".format((System.currentTimeMillis() - startTime) / 1000.0)}] ${
          message.replace(
            "\n",
            "\n\t"
          )
        }\n".toByteArray()
      )
      stream.flush()
    }
    when (level) {
      Level.ERROR -> log.error(message)
      Level.WARN -> log.warn(message)
      Level.INFO -> log.info(message)
      Level.DEBUG -> log.debug(message)
      Level.TRACE -> log.debug(message)
      else -> log.debug(message)
    }
  }

}