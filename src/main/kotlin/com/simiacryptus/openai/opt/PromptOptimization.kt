package com.simiacryptus.openai.opt

import com.simiacryptus.openai.OpenAIClient
import com.simiacryptus.openai.proxy.ChatProxy
import com.simiacryptus.util.describe.Description
import org.slf4j.LoggerFactory

open class PromptOptimization(
    val api: OpenAIClient,
    val model: OpenAIClient.Models = OpenAIClient.Models.GPT35Turbo
) {

    data class TestCase(val turns: List<Turn>, val retries: Int = 3)

    data class Turn(val userMessage: String, val expectations: List<Expectation>)

    open fun runGeneticGenerations(
        systemPrompts: List<String>,
        testCases: List<TestCase>,
        selectionSize: Int = Math.max(Math.ceil(Math.log((systemPrompts.size + 1).toDouble()) / Math.log(2.0)), 3.0)
            .toInt(), // apx ln(N)
        generations: Int = 3
    ): List<String> {
        val populationSize = Math.max(Math.max(selectionSize, 5), systemPrompts.size)
        var topPrompts = regenerate(systemPrompts, populationSize)
        for (generation in 0..generations) {
            val scores = topPrompts.map { prompt ->
                val score = testCases.map { testCase ->
                    evaluate(prompt, testCase)
                }.average()
                log.info("Scored $prompt -> $score")
                prompt to score
            }
            val survivors = scores.sortedByDescending { it.second }.take(selectionSize).map { it.first }
            topPrompts = regenerate(survivors, populationSize)
            println("Generation $generation: ${topPrompts.first()}")
        }
        return topPrompts
    }

    open fun regenerate(progenetors: List<String>, desiredCount: Int): List<String> {
        val result = listOf<String>().toMutableList()
        result += progenetors
        while (result.size < desiredCount) {
            if (progenetors.size == 1) {
                val selected = progenetors.first()
                val mutated = mutate(selected)
                result += mutated
            } else if (progenetors.size == 0) {
                throw RuntimeException("No survivors")
            } else {
                val a = progenetors.random()
                var b: String
                do {
                    b = progenetors.random()
                } while (a == b)
                val child = recombine(a, b)
                result += child
            }
        }
        return result
    }

    open fun recombine(a: String, b: String): String {
        for(retry in 0..3) {
            try {
                val child = geneticApi.recombine(a, b)
                if(child.contains(a) || child.contains(b)) {
                    log.info("Recombine failure: retry $retry")
                    continue
                }
                log.info("Recombined $a + $b -> $child")
                return child
            } catch (e: Exception) {
                log.warn("Failed to recombine $a + $b", e)
            }
        }
        return mutate(a)
    }

    open fun mutate(selected: String): String {
        for(retry in 0..3) {
            try {
                val mutated = geneticApi.mutate(selected)
                if(mutated.contains(selected)) {
                    log.info("Mutate failure: retry $retry")
                    continue
                }
                log.info("Mutated $selected -> $mutated")
                return mutated
            } catch (e: Exception) {
                log.warn("Failed to mutate $selected", e)
            }
        }
        throw RuntimeException("Failed to mutate $selected")
    }

    protected interface GeneticApi {
        @Description("Mutate the given prompt; rephrase, make random edits, etc.")
        fun mutate(
            systemPrompt: String
        ): String

        @Description("Recombine the given prompts to produce a third with about the same length; swap phrases, reword, etc.")
        fun recombine(
            systemPromptA: String,
            systemPromptB: String
        ): String
    }


    protected open val geneticApi = ChatProxy(
        clazz = GeneticApi::class.java,
        api = api,
        model = model,
    ).create()

    open fun evaluate(systemPrompt: String, testCase: TestCase): Double {
        return run(systemPrompt, testCase).map { it.second }.average()
    }

    open fun run(
        systemPrompt: String,
        testCase: TestCase
    ): List<Pair<OpenAIClient.ChatResponse, Double>> {
        val chatRequest = OpenAIClient.ChatRequest(
            model = model.modelName,
            temperature = 0.3
        )
        var response = OpenAIClient.ChatResponse()
        chatRequest.messages += OpenAIClient.ChatMessage(
            OpenAIClient.ChatMessage.Role.system,
            systemPrompt
        )
        return testCase.turns.map { turn ->
            var matched = false
            chatRequest.messages += OpenAIClient.ChatMessage(
                OpenAIClient.ChatMessage.Role.user,
                turn.userMessage
            )
            for (i in 0..testCase.retries) {
                response = api.chat(chatRequest, model)
                matched = turn.expectations.all { it.matches(api, response) }
                if (matched) {
                    break
                } else {
                    log.info("Retry $i: $systemPrompt / ${turn.userMessage} -> ${response.choices.first().message?.content}")
                }
            }
            chatRequest.messages += OpenAIClient.ChatMessage(
                OpenAIClient.ChatMessage.Role.assistant,
                response.choices.first().message?.content ?: ""
            )
            if (!matched) {
                response to 0.0
            } else {
                response to turn.expectations.map { it.score(api, response) }.average()
            }
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(PromptOptimization::class.java)
    }

}
