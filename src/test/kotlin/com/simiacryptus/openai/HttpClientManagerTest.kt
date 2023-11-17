package com.simiacryptus.openai

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.TimeUnit

class HttpClientManagerTest {

    private lateinit var server: MockWebServer
    private lateinit var httpClientManager: HttpClientManager

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        httpClientManager = HttpClientManager() // Modify HttpClientManager to accept base URL
    }

    @AfterEach
    fun tearDown() {
        Thread { server.shutdown() }.start()
    }

    @Test
    fun testCancellation() {
        server.enqueue(MockResponse().setBody("Delayed response").setBodyDelay(30, TimeUnit.SECONDS))
        var cancelled = false
        var ex : Exception? = null
        val thread = Thread {
            try {
                httpClientManager.withCancellationMonitor(
                    {
                        httpClientManager.withClient { httpClient ->
                            val uri = server.url("/").toUri()
                            httpClient.execute(HttpGet(uri)).entity.content.readAllBytes()
                            null
                        }
                    },
                    { cancelled }
                )
            } catch (e: Exception) {
                ex = e
            }
        }
        thread.start()
        Thread.sleep(1000)
        cancelled = true
        thread.join()
        assertTrue(ex is InterruptedException, "Exception should be an InterruptedException; was ${ex?.javaClass?.name}")
    }

    @Test
    fun testTimeout() {
        server.enqueue(MockResponse().setBody("Delayed response").setBodyDelay(30, TimeUnit.SECONDS))
        val exception = try {
            httpClientManager.withTimeout(Duration.ofSeconds(1)) {
                httpClientManager.withClient { httpClient ->
                    log.info("Executing request")
                    val uri = server.url("/").toUri()
                    httpClient.execute(HttpGet(uri)).entity.content.readAllBytes()
                    log.info("Request executed")
                    null
                }
            }
        } catch (e : Throwable) {
            log.info("Request failed with exception", e)
            e
        }
        assertTrue(exception is InterruptedException, "Exception cause should be an InterruptedException; was ${exception}")
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(HttpClientManagerTest::class.java)
    }

}