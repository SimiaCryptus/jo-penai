import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import kotlin.test.assertNotNull

@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class OpenAIClientIntegrationTest {
    private lateinit var openAIClient: OpenAIClient

    @BeforeEach
    fun setup() {
        val apiKey = System.getenv("OPENAI_API_KEY")
        openAIClient = OpenAIClient(mapOf("api_key" to apiKey))
    }

    @Test
    fun `test listEngines integration`() {
        val engines = openAIClient.listEngines()
        assertNotNull(engines)
        // Add more assertions to validate the response
    }

    @Test
    fun `test createCompletion integration`() {
        val completionRequest = ApiModel.CompletionRequest(
            model = "text-davinci-002",
            prompt = "Once upon a time",
            maxTokens = 50
        )
        val completion = openAIClient.complete(completionRequest, OpenAITextModel.DAVINCI)
        assertNotNull(completion)
        // Add more assertions to validate the response
    }

    @Test
    fun `test createChatCompletion integration`() {
        val chatRequest = ApiModel.ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                ApiModel.ChatMessage(
                    role = "system",
                    content = "You are a helpful assistant."
                ),
                ApiModel.ChatMessage(
                    role = "user",
                    content = "What is the capital of France?"
                )
            )
        )
        val chatResponse = openAIClient.chat(chatRequest, ChatModels.GPT_3_5_TURBO)
        assertNotNull(chatResponse)
        // Add more assertions to validate the response
    }

    // Add more integration test methods for other OpenAIClient functionalities
}
