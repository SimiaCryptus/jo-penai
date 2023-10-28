package com.simiacryptus.openai

import com.simiacryptus.openai.opt.Expectation
import com.simiacryptus.openai.opt.PromptOptimization
import com.simiacryptus.openai.opt.PromptOptimization.TestCase
import com.simiacryptus.openai.opt.PromptOptimization.Turn
import org.slf4j.event.Level

object PromptOptTest {

    @JvmStatic
    fun main( args: Array<String>) {
        try {
            PromptOptimization(
                OpenAIClient(
                    logLevel = Level.DEBUG
                )
            ).runGeneticGenerations(
                systemPrompts = listOf(
                    """
                    |You are a search assistant; you interpret user requests and return search results. 
                    |You are given a user request and respond with a call to `search("query text")`.
                    |""".trimMargin().trim(),
                    """
                    |You generate search queries for a user, acting as an intermediary between the user and the search engine.
                    |Respond using `search("query text")` to return search results.
                    |""".trimMargin().trim(),
                    """
                    |You act as a bridge between the user and the search engine by creating search queries. 
                    |Use `search("query text")` to provide search results.
                    |""".trimMargin().trim(),
                ), testCases = listOf(
                    TestCase(
                        turns = listOf(
                            Turn(
                                "I want to buy a book.",
                                listOf(
                                    Expectation.ContainsMatch("`search('.*')`".toRegex(), critical = false),
                                    Expectation.VectorMatch("Great, what kind of book are you looking for?")
                                )
                            )
                        )
                    )
                )
            )
        } finally {
            System.exit(0)
        }
    }

}