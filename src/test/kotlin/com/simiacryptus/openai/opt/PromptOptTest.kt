package com.simiacryptus.openai.opt

import com.simiacryptus.openai.OpenAIClient
import com.simiacryptus.openai.opt.PromptOptimization.TestCase
import com.simiacryptus.openai.opt.PromptOptimization.Turn
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

object PromptOptTest {

    private val log = LoggerFactory.getLogger(PromptOptTest::class.java)

    @JvmStatic
    fun main( args: Array<String>) {
        try {
            PromptOptimization(
                OpenAIClient(
                    logLevel = Level.DEBUG
                )
            ).runGeneticGenerations(
                populationSize = 7,
                generations = 5,
                systemPrompts = listOf(
                    """
                    |As the intermediary between the user and the search engine, your main task is to generate search queries based on user requests. 
                    |Please respond to each user request by providing one or more calls to the "`search('query text')`" function.
                    |""".trimMargin(),
                    """
                    |You act as a bridge between the user and the search engine by creating search queries. 
                    |Output one or more calls to "`search('query text')`" in response to each user request.
                    |""".trimMargin().trim(),
                    """
                    |You play the role of a search assistant. 
                    |Provide one or more "`search('query text')`" calls as a response to each user request.
                    |Make sure to use single quotes around the query text.
                    |Surround the search function call with backticks.
                    |""".trimMargin().trim(),
                ), testCases = listOf(
                    TestCase(
                        turns = listOf(
                            Turn(
                                "I want to buy a book.",
                                listOf(
                                    Expectation.ContainsMatch("""`search\('.*?'\)`""".toRegex(), critical = false),
                                    Expectation.ContainsMatch("""search\(.*?\)""".toRegex(), critical = false),
                                    Expectation.VectorMatch("Great, what kind of book are you looking for?")
                                )
                            ),
                            Turn(
                                "A history book about Napoleon.",
                                listOf(
                                    Expectation.ContainsMatch("""`search\('.*?'\)`""".toRegex(), critical = true),
                                    Expectation.ContainsMatch("""search\(.*?\)""".toRegex(), critical = false),
                                )
                            )
                        )
                    )
                )
            )
        } catch (e: Throwable) {
            log.error("Error", e)
        } finally {
            System.exit(0)
        }
    }

}