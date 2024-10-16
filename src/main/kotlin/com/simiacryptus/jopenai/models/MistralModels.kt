package com.simiacryptus.jopenai.models

object MistralModels {

    val Mistral7B = ChatModel(
        name = "Mistral7B",
        modelName = "open-mistral-7b",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val Mixtral8x7B = ChatModel(
        name = "Mixtral8x7B",
        modelName = "open-mixtral-8x7b",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val Mixtral8x22B = ChatModel(
        name = "Mixtral8x22B",
        modelName = "open-mixtral-8x22b",
        maxTotalTokens = 65536,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val MistralSmall = ChatModel(
        name = "MistralSmall",
        modelName = "mistral-small-latest",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val MistralMedium = ChatModel(
        name = "MistralMedium",
        modelName = "mistral-medium-latest",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val MistralLarge = ChatModel(
        name = "MistralLarge",
        modelName = "mistral-large-latest",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val MistralNemo = ChatModel(
        name = "MistralNemo",
        modelName = "open-mistral-nemo",
        maxTotalTokens = 128 * 1024 - 1,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val Codestral = ChatModel(
        name = "Codestral",
        modelName = "codestral-latest",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val CodestralMamba = ChatModel(
        name = "CodestralMamba",
        modelName = "open-codestral-mamba",
        maxTotalTokens = 128 * 1024 - 1,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )

}