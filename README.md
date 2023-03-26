# JoePenai - Java OpenAI API Client

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.simiacryptus/joe-penai/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.simiacryptus/joe-penai)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

<!-- TOC -->

* [JoePenai - Java OpenAI API Client](#joepenai---java-openai-api-client)
    * [Basic Features](#basic-features)
    * [GPT-Proxy API](#gpt-proxy-api)
        * [Key Features](#key-features)
        * [Getting Started](#getting-started)
        * [Examples](#examples)
            * [Example 1: Simple Interface](#example-1--simple-interface)
            * [Example 2: Adding Metadata and Annotations](#example-2--adding-metadata-and-annotations)
            * [Example 3: Using Kotlin Data Classes](#example-3--using-kotlin-data-classes)
            * [Example 4: Text Parsing](#example-4--text-parsing)
            * [Example 5: Adding Examples](#example-5--adding-examples)
            * [Example 6: Adding Validation Rules](#example-6--adding-validation-rules)
        * [Best Practices](#best-practices)
        * [Internal Details](#internal-details)

<!-- TOC -->

This is an OpenAI API client for Java, built with Kotlin. It provides a simple interface for interacting with OpenAI's
API, allowing access to text completions, edits, chats, dictations, and renderings.

## Basic Features

| Feature         | Description                             |
|-----------------|-----------------------------------------|
| Text Completion | Generate text completions from a prompt |
| Text Editing    | Generate text edits from a prompt       |
| Text Chat       | Generate text responses to a prompt     |
| Text Dictation  | Generate text dictations from audio     |
| Text Rendering  | Generate images from a prompt           |

## GPT-Proxy API

Built on top of the OpenAI API, JoePenai also provides a Proxy interface.
This allows any Java/Kotlin interface to be serviced by a GPT model.
It improves the chat api by adding type structures and metadata to both the request and response.
It allows description annotations, example requests/responses, and response validation to be added to the interface.
For example, you can easily parse plaintext into complex data structures by simply by providing a requested structure
and text.

With the GPT-Proxy API, you can now enjoy a more structured and efficient way of interacting with GPT models. This
powerful tool enhances your API experience by providing clear and consistent data types, metadata, and annotations. Say
goodbye to the hassle of prompt engineering and hello to scalability and improved content generation. Get started today
and unlock the full potential of the GPT-Proxy API!

### Key Features

1. **Strongly typed requests and responses** - Improve the clarity and consistency of your API by defining the expected
   input and output types.
2. **Metadata and annotations** - Add descriptions, examples, and validation rules to your interface, making it easier
   for
   developers to understand and use your API effectively.
3. **Automatic prompt generation** - Simplify prompt engineering by automatically generating prompts based on the
   provided
   interface and annotations.
4. **Scalability** - Manage multiple requests and data subsets efficiently, enabling higher-scale content generation and
   structured parsing.

### Getting Started

To start using the GPT-Proxy API, follow these steps:

1. Define your Java/Kotlin interface with the desired input and output types.
2. Create the proxy class using the GPT-Proxy API, specifying the interface and any necessary configuration.
3. Test your new API, and iteratively improve the names, annotations, examples, and validation rules.
4. ???
5. Profit!

### Examples

Here are some examples of how to use the GPT-Proxy API:

#### Example 1: Simple Interface

Define a simple interface for generating a summary of a given text:

```kotlin
interface TextSummarizer {
    fun summarize(text: String): String
}
```

Create the proxy class and use it:

```kotlin
val gptProxy = ChatProxy(TextSummarizer::class.java, "...apikey...")
val summarizer = gptProxy.create()
val summary = summarizer.summarize("Your long text goes here.")
println(summary)
```

This returned for me:

```json
{
  "summary": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sed mi vel libero aliquet feugiat sit amet faucibus enim. Sed vel velit commodo, lobortis leo vitae, lobortis nisi. Donec sollicitudin nunc a pretium bibendum. Fusce ac dui vel lectus facilisis commodo in vel justo. Sed at dapibus dolor, quis euismod mi. Sed eget rhoncus urna, vel cursus velit. Sed ac nunc in ex dictum ornare. Sed vitae rutrum nisi. Nullam sit amet nisi id urna dapibus iaculis. Sed malesuada, nisl a lacinia dignissim, ligula nisi maximus ligula, sed convallis libero sapien vel nulla. Curabitur auctor est in urna suscipit, et vehicula sapien varius. Sed convallis arcu sit amet quam bibendum, ut facilisis felis congue. Nullam ac magna id sapien blandit faucibus et sed magna. Integer vel mi eget odio luctus faucibus."
}
```

Notice; even though a string was requested, the response was a JSON object with a `summary` field.
Such is the nature of the GPT model, be warned!

#### Example 2: Adding Metadata and Annotations

Define a more complex interface with metadata and annotations:

```kotlin
interface BlogPostGenerator {
    @Description("Generate a blog post based on the given title and keywords.")
    fun generateBlogPost(
        @Description("The title of the blog post.") title: String,
        @Description("A list of keywords related to the blog post.") keywords: List<String>
    ): String
}
```

Create the proxy class and use it:

```kotlin
val gptProxy = ChatProxy(BlogPostGenerator::class.java, "...apikey...")
val generator = gptProxy.create()
val blogPost = generator.generateBlogPost(
    "The Future of AI",
    listOf("artificial intelligence", "machine learning", "deep learning")
)
println(blogPost)
```

This returned for me:

```text
Here is a sample response:

"The Future of AI

Artificial Intelligence (AI) is quickly becoming one of the most important technological advancements of our time. With the rise of machine learning and deep learning, AI is poised to revolutionize the way we work, live, and interact with each other.

Advances in AI have already led to significant improvements in fields such as healthcare, finance, and transportation. As AI continues to evolve, we can expect to see even more exciting developments in the years ahead.

So what does the future of AI hold? Some experts predict that we will see even more sophisticated AI systems that are capable of performing increasingly complex tasks. Others believe that AI will eventually surpass human intelligence, leading to a world where machines are in control.

Regardless of what the future holds, it is clear that AI will play a major role in shaping the world of tomorrow. As we continue to explore the possibilities of this remarkable technology, we can look forward to a future that is more efficient, more productive, and more innovative than ever before."
```

Notice, again, requesting a single string type results in GPT tending to "frame" the response.
When using a strongly typed response, I have found this to be less of an issue.

#### Example 3: Using Kotlin Data Classes

Define an interface using Kotlin data classes for input and output types:

```kotlin
    data class RecipeInput(
    val title: String = "",
    val ingredients: List<String> = listOf(),
    val servings: Int = 0
)

data class RecipeOutput(
    val title: String = "",
    val ingredients: List<String> = listOf()
    val instructions: List<String> = listOf(),
    val servings: Int = 0
)

interface RecipeGenerator {
    fun generateRecipe(input: RecipeInput): RecipeOutput
}
```

Create the proxy class and use it:

```kotlin
val gptProxy = ChatProxy(RecipeGenerator::class.java, "...apikey...")
val generator = gptProxy.create()
val recipeInput = RecipeInput("Chocolate Cake", listOf("flour", "sugar", "cocoa powder", "eggs", "milk"), 8)
val recipeOutput = generator.generateRecipe(recipeInput)
println(recipeOutput)
```

This returns a reliably good no-nonsense response:

```json
{
  "ingredients": [
    "flour",
    "sugar",
    "cocoa powder",
    "eggs",
    "milk"
  ],
  "instructions": [
    "Preheat the oven to 350°F (175°C).",
    "In a mixing bowl, combine flour, sugar, and cocoa powder.",
    "Add eggs and milk. Mix well.",
    "Pour the batter into a greased cake pan.",
    "Bake for 25-30 minutes or until a toothpick inserted into the center of the cake comes out clean.",
    "Let the cake cool before serving."
  ],
  "servings": 8,
  "title": "Chocolate Cake"
}
```

#### Example 4: Text Parsing

Define an interface for parsing a plain text shopping list into a structured data format:

```kotlin
data class ShoppingItem(
    var name: String = "",
    var quantity: Int = 0
)

interface ShoppingListParser {
    @Description("Parse a plain text shopping list into a structured data format.")
    fun parseShoppingList(@Description("The plain text shopping list.") text: String): List<ShoppingItem>
}
```

Create the proxy class and use it:

```kotlin
val gptProxy = ChatProxy(ShoppingListParser::class.java, "...apikey...")
val parser = gptProxy.create()
val plainTextList = "2 apples\n1 loaf of bread\n3 cans of soup\n4 bananas"
val parsedList = parser.parseShoppingList(plainTextList)
println(parsedList)
```

This returns for me:

```json
{
  "items": {
    "apples": 2,
    "loaf of bread": 1,
    "cans of soup": 3,
    "bananas": 4
  }
}
```

#### Example 5: Adding Examples

Define an interface:

```kotlin
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
```

Create the proxy class, add examples, and use it:

```kotlin
val gptProxy = ChatProxy(WeatherForecast::class.java, "...apikey...")
gptProxy.addExample(WeatherOutput("New York", listOf("Sunny", "Partly Cloudy", "Rainy"))) {
    it.getWeatherForecast(WeatherInput("New York", 3))
}
val weather = gptProxy.create()
val weatherInput = WeatherInput("Seattle", 3)
val weatherOutput = weather.getWeatherForecast(weatherInput)
println(weatherOutput)
```

This returns for me:

```json
{
  "city": "Seattle",
  "forecast": [
    "Rainy",
    "Rainy",
    "Cloudy"
  ]
}
```

#### Example 6: Adding Validation Rules

Validation rules are important for defining what an "acceptable" response looks like.
This helps manage your API and take advantage of the client's retry and temperature management features.

Define an interface with validation rules:

```kotlin
data class MathProblem(
    @Description("The math problem to solve.") val problem: String = ""
)

data class MathSolution(
    @Description("The solution to the math problem.") val solution: String = ""
) : ValidatedObject {
    override fun validate(): Boolean {
        if (solution.isEmpty()) return false
        return super.validate()
    }
}

interface MathSolver {
    @Description("Solve a given math problem.")
    @ValidateResponse("response.solution.isNotEmpty()")
    fun solveMathProblem(input: MathProblem): MathSolution
}
```

Create the proxy class and use it:

```kotlin
val gptProxy = ChatProxy(MathSolver::class.java, "...apikey...")
val solver = gptProxy.create()
val mathInput = MathProblem("twelve pigs plus six cows minus four pigs")
val mathOutput = solver.solveMathProblem(mathInput)
println(mathOutput)
```

This returns for me, with ChatGPT:

```json
{
  "solution": "ten pigs plus six cows"
}
```

GPT4, configured like this:

```kotlin
if (!keyFile.exists()) return
val gptProxy = ChatProxy(MathSolver::class.java, apiKey)
gptProxy.model = "gpt-4-0314"
val solver = gptProxy.create()
val mathInput = MathProblem("twelve pigs plus six cows minus four pigs")
val mathOutput = solver.solveMathProblem(mathInput)
println(mathOutput)
```

Returned:

```json
{
  "solution": "8 pigs + 6 cows"
}
```

In this example, the validation rule ensures that the response contains a non-empty solution.
If the GPT model returns an empty solution or an invalid response,
the API will automatically retry the request with adjusted parameters (e.g., higher temperature)
until it receives a valid response or reaches the maximum number of retries.

By incorporating validation rules, you can improve the reliability and quality of the responses generated by the
GPT-Proxy API. This helps ensure that your application receives accurate and useful information from the GPT model,
ultimately enhancing the user experience.

Remember to follow best practices and iteratively test and improve your interface for optimal results. With the
GPT-Proxy API, you can unlock the full potential of GPT models and create powerful, structured, and scalable
applications.

These examples demonstrate the versatility and ease of use of the GPT-Proxy API. By following best practices and
leveraging the power of Kotlin data classes, metadata, and annotations, you can create powerful and efficient APIs for a
wide range of applications.

### Best Practices

When using the GPT-Proxy API, there are a few best practices to keep in mind:

1. Use Kotlin data classes to define your input and output types.
   All fields should have default values, and all fields should be mutable.
   Do not use accessor methods (e.g. `get` and `set`).
2. Ensure your build system is retaining parameter metadata.
   For example, your gradle build should include `compileKotlin { kotlinOptions { javaParameters = true }}`
3. Do not use collection return types.
   Although the protocol supports them, the GPT model tends to want to return a wrapper object.
   For example, use `MyWidgets` and an extra data class instead of `List<MyWidget>` as a return type.
4. Keep your interface simple and consistent.
   Give each call enough information to be useful, but not so much that it becomes confused.
5. Use descriptive names and annotations to make your API easier to understand both for developers and for GPT models.
6. Test your API iteratively, and adjust your interface as needed.
   Pay attention to malformed responses and examine why they are occurring.
7. Use the `@Description` annotation to provide a description of the data structure.
8. Use the `addExample` call to provide example requests and responses.
9. Monitor your API performance via the metrics method.

### Internal Details

How does the GPT-Proxy API work? The GPT-Proxy API uses a combination of reflection and annotation processing to
generate
a proxy class that implements the desired interface. The proxy class is then used to interact with the GPT model.

When a method is called on the proxy class, the following steps are performed:

1. The method call is converted to a chat request
    1. The method signature is converted into an OpenAPI-like YAML description
    2. Any examples for this method are serialized into JSON
    3. Method arguments are converted into a JSON object
    4. These are combined into a Chat request
2. The call is made and the response is parsed into a JSON object
3. If there is a parsing or validation error, the request is retried, possibly with a higher temperature
4. The JSON object is converted into the desired output type and returned
