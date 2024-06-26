package com.simiacryptus.jopenai

import com.simiacryptus.jopenai.describe.Description
import com.simiacryptus.jopenai.models.APIProvider
import com.simiacryptus.jopenai.models.ChatModels
import com.simiacryptus.jopenai.proxy.ChatProxy
import com.simiacryptus.jopenai.proxy.ValidatedObject
import com.simiacryptus.jopenai.util.ClientUtil
import org.junit.jupiter.api.Test

class ChatProxyTest {

    interface TextSummarizer {
        fun summarize(text: String): String
    }

    @Test
    fun testDemo1() {
        val prov = ClientUtil.keyMap[ClientUtil.defaultApiProvider.name] ?: return
        if (prov.isBlank() == true) return
        val gptProxy = ChatProxy(
            clazz = TextSummarizer::class.java,
            api = OpenAIClient(
                mapOf(
                    ClientUtil.defaultApiProvider to prov
                )
            ),
            model = ChatModels.GPT35Turbo
        )
        val summarizer = gptProxy.create()
        val summary = summarizer.summarize("Your long text goes here.")
        println(summary)
    }

    interface BlogPostGenerator {
        @Description("Generate a blog post based on the given title and keywords.")
        fun generateBlogPost(
            @Description("The title of the blog post.") title: String,
            @Description("A list of keywords related to the blog post.") keywords: List<String>
        ): String
    }

    @Test
    fun testDemo2() {
        val prov = ClientUtil.keyMap[ClientUtil.defaultApiProvider.name] ?: return
        if (prov.isBlank() == true) return
        val gptProxy = ChatProxy(

            clazz = BlogPostGenerator::class.java,
            api = OpenAIClient(ClientUtil.keyMap.mapKeys { APIProvider.valueOf(it.key) }),
            model = ChatModels.GPT35Turbo
        )
        val generator = gptProxy.create()
        val blogPost = generator.generateBlogPost(
            "The Future of AI",
            listOf("artificial intelligence", "machine learning", "deep learning")
        )
        println(blogPost)
    }

    data class RecipeInput(
        val title: String = "",
        val ingredients: List<String> = listOf(),
        val servings: Int = 0
    )

    data class RecipeOutput(
        val title: String = "",
        val ingredients: Map<String, String> = mapOf(),
        val instructions: List<String> = listOf(),
        val servings: Int = 0
    )

    interface RecipeGenerator {
        fun generateRecipe(input: RecipeInput): RecipeOutput
    }

    @Test
    fun testDemo3() {
        val prov = ClientUtil.keyMap[ClientUtil.defaultApiProvider.name] ?: return
        if (prov.isBlank() == true) return
        val gptProxy = ChatProxy(
            clazz = RecipeGenerator::class.java,
            api = OpenAIClient(ClientUtil.keyMap.mapKeys { APIProvider.valueOf(it.key) }),
            model = ChatModels.GPT35Turbo
        )
        val generator = gptProxy.create()
        val recipeInput = RecipeInput("Chocolate Cake", listOf("flour", "sugar", "cocoa powder", "eggs", "milk"), 8)
        val recipeOutput = generator.generateRecipe(recipeInput)
        println(recipeOutput)
    }

    interface ShoppingListParser {
        @Description("Parse a plain text shopping list into a structured data format.")
        fun parseShoppingList(@Description("The plain text shopping list.") text: String): ShoppingItems
    }

    data class ShoppingItems(
        var items: Map<String, Int> = mapOf()
    )

    @Test
    fun testDemo4() {
        val prov = ClientUtil.keyMap[ClientUtil.defaultApiProvider.name] ?: return
        if (prov.isBlank() == true) return
        val gptProxy = ChatProxy(

            clazz = ShoppingListParser::class.java,
            api = OpenAIClient(ClientUtil.keyMap.mapKeys { APIProvider.valueOf(it.key) }),
            model = ChatModels.GPT35Turbo
        )
        val parser = gptProxy.create()
        val plainTextList = "2 apples\n1 loaf of bread\n3 cans of soup\n4 bananas"
        val parsedList = parser.parseShoppingList(plainTextList)
        println(parsedList)
    }

    data class WeatherInput(
        @Description("The city for which to get the weather forecast.") val city: String = "",
        @Description("The number of days for the forecast.") val days: Int = 0
    )

    data class WeatherOutput(
        @Description("The city for which the forecast is provided.") val city: String = "",
        @Description("The weather forecast for the specified number of days.") val forecast: List<String> = listOf()
    )

    interface WeatherForecast {
        @Description("Get a weather forecast for a specified city and number of days.")
        fun getWeatherForecast(input: WeatherInput): WeatherOutput
    }

    @Test
    fun testDemo5() {
        val prov = ClientUtil.keyMap[ClientUtil.defaultApiProvider.name] ?: return
        if (prov.isBlank() == true) return
        val gptProxy = ChatProxy(
            clazz = WeatherForecast::class.java,
            api = OpenAIClient(ClientUtil.keyMap.mapKeys { APIProvider.valueOf(it.key) }),
            model = ChatModels.GPT35Turbo
        )
        gptProxy.addExample(WeatherOutput("New York", listOf("Sunny", "Partly Cloudy", "Rainy"))) {
            it.getWeatherForecast(WeatherInput("New York", 3))
        }
        val weather = gptProxy.create()
        val weatherInput = WeatherInput("Seattle", 3)
        val weatherOutput = weather.getWeatherForecast(weatherInput)
        println(weatherOutput)
    }

    data class MathProblem(
        @Description("The math problem to solve.") val problem: String = ""
    )

    data class MathSolution(
        @Description("The solution to the math problem.") val solution: String = ""
    ) : ValidatedObject {
        override fun validate(): String? {
            if (solution.isEmpty()) return "Solution is empty"
            return super.validate()
        }
    }

    interface MathSolver {
        @Description("Solve a given math problem.")
        fun solveMathProblem(input: MathProblem): MathSolution
    }

    @Test
    fun testDemo6() {
        val prov = ClientUtil.keyMap[ClientUtil.defaultApiProvider.name] ?: return
        if (prov.isBlank() == true) return
        val gptProxy = ChatProxy(
            clazz = MathSolver::class.java,
            api = OpenAIClient(ClientUtil.keyMap.mapKeys { APIProvider.valueOf(it.key) }),
            model = ChatModels.GPT35Turbo
        )
        val solver = gptProxy.create()
        val mathInput = MathProblem("twelve pigs plus six cows minus four pigs")
        val mathOutput = solver.solveMathProblem(mathInput)
        println(mathOutput)
    }

    @Test
    fun testDemo7() {
        val prov = ClientUtil.keyMap[ClientUtil.defaultApiProvider.name] ?: return
        if (prov.isBlank() == true) return
        val gptProxy =
            ChatProxy(
                MathSolver::class.java, api = OpenAIClient(
                    mapOf(
                        ClientUtil.defaultApiProvider to prov
                    )
                ), model = ChatModels.GPT35Turbo
            )
//        gptProxy.model = ChatModels.GPT35Turbo
        val solver = gptProxy.create()
        val mathInput = MathProblem("twelve pigs plus six cows minus four pigs")
        val mathOutput = solver.solveMathProblem(mathInput)
        println(mathOutput)
    }
}
