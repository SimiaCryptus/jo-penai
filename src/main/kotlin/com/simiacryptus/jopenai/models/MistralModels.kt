package com.simiacryptus.jopenai.models
import org.slf4j.LoggerFactory

object MistralModels {
    private val logger = LoggerFactory.getLogger(MistralModels::class.java)
    init {
        logger.info("Initializing MistralModels object")
    }


    val Mistral7B = ChatModel(
        name = "Mistral7B",
        modelName = "open-mistral-7b",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    private fun logModelInitialization(model: ChatModel) {
        logger.info("Initialized ${model.name} model with max tokens: ${model.maxTotalTokens}")
    }
    init {
        logger.info("Initialized Mistral7B model with max tokens: ${Mistral7B.maxTotalTokens}")
        logModelInitialization(Mistral7B)
    }
    val Mixtral8x7B = ChatModel(
        name = "Mixtral8x7B",
        modelName = "open-mixtral-8x7b",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    init {
        logModelInitialization(Mixtral8x7B)
    }
    val Mixtral8x22B = ChatModel(
        name = "Mixtral8x22B",
        modelName = "open-mixtral-8x22b",
        maxTotalTokens = 65536,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    init {
        logModelInitialization(Mixtral8x22B)
    }
    val MistralSmall = ChatModel(
        name = "MistralSmall",
        modelName = "mistral-small-latest",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    init {
        logModelInitialization(MistralSmall)
    }
    val MistralMedium = ChatModel(
        name = "MistralMedium",
        modelName = "mistral-medium-latest",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    init {
        logModelInitialization(MistralMedium)
    }
    val MistralLarge = ChatModel(
        name = "MistralLarge",
        modelName = "mistral-large-latest",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    init {
        logModelInitialization(MistralLarge)
    }
    val MistralNemo = ChatModel(
        name = "MistralNemo",
        modelName = "open-mistral-nemo",
        maxTotalTokens = 128 * 1024 - 1,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    init {
        logModelInitialization(MistralNemo)
    }
    val Codestral = ChatModel(
        name = "Codestral",
        modelName = "codestral-latest",
        maxTotalTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    init {
        logModelInitialization(Codestral)
    }
    val CodestralMamba = ChatModel(
        name = "CodestralMamba",
        modelName = "open-codestral-mamba",
        maxTotalTokens = 128 * 1024 - 1,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    init {
        logModelInitialization(CodestralMamba)
    }
    val values = mapOf(
        "Mistral7B" to Mistral7B,
        "Mixtral8x7B" to Mixtral8x7B,
        "Mixtral8x22B" to Mixtral8x22B,
        "MistralSmall" to MistralSmall,
        "MistralMedium" to MistralMedium,
        "MistralLarge" to MistralLarge,
        "MistralNemo" to MistralNemo,
        "Codestral" to Codestral,
        "CodestralMamba" to CodestralMamba,
    )

}