package com.simiacryptus.jopenai.models

object AWSModels {

    val AWSLLaMA31_405bChat = ChatModels(
        name = "AWSLLaMA31_405bChat",
        modelName = "meta.llama3-1-405b-instruct-v1:0",
        maxTotalTokens = 128 * 1024 - 1,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.00195,
        outputTokenPricePerK = 0.00256
    )
    val AWSLLaMA31_70bChat = ChatModels(
        name = "AWSLLaMA31_70bChat",
        modelName = "meta.llama3-1-70b-instruct-v1:0",
        maxTotalTokens = 128 * 1024 - 1,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.00195,
        outputTokenPricePerK = 0.00256
    )
    val AWSLLaMA31_8bChat = ChatModels(
        name = "AWSLLaMA31_8bChat",
        modelName = "meta.llama3-1-8b-instruct-v1:0",
        maxTotalTokens = 128 * 1024 - 1,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.00195,
        outputTokenPricePerK = 0.00256
    )


    val AWSLLaMA270bChat = ChatModels(
        name = "AWSLLaMA270bChat",
        modelName = "meta.llama2-70b-chat-v1",
        maxTotalTokens = 2048,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.00195,
        outputTokenPricePerK = 0.00256
    )
    val AWSLLaMA213bChat = ChatModels(
        name = "AWSLLaMA213bChat",
        modelName = "meta.llama2-13b-chat-v1",
        maxTotalTokens = 2048,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.00075,
        outputTokenPricePerK = 0.001
    )
    val Mistral7bInstructV02 = ChatModels(
        name = "Mistral7bInstructV02",
        modelName = "mistral.mistral-7b-instruct-v0:2",
        maxTotalTokens = 32 * 1024,
        maxOutTokens = 2 * 1024,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.00015,
        outputTokenPricePerK = 0.0002
    )
    val Mixtral8x7bInstructV01AWS = ChatModels(
        name = "Mixtral8x7bInstructV01AWS",
        modelName = "mistral.mixtral-8x7b-instruct-v0:1",
        maxTotalTokens = 32 * 1024,
        maxOutTokens = 2 * 1024,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.00045,
        outputTokenPricePerK = 0.0007
    )
    val MistralLarge2402 = ChatModels(
        name = "MistralLarge2402",
        modelName = "mistral.mistral-large-2402-v1:0",
        maxTotalTokens = 32 * 1024,
        maxOutTokens = 4000,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.008,
        outputTokenPricePerK = 0.024
    )
    val MistralLarge2407 = ChatModels(
        name = "MistralLarge2407",
        modelName = "mistral.mistral-large-2407-v1:0",
        maxTotalTokens = 32 * 1024,
        maxOutTokens = 4000,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.008,
        outputTokenPricePerK = 0.024
    )

    val AmazonTitanTextLiteV1 = ChatModels(
        name = "AmazonTitanTextLiteV1",
        modelName = "amazon.titan-text-lite-v1",
        maxTotalTokens = 4096,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.0003,
        outputTokenPricePerK = 0.0004
    )
    val AmazonTitanTextExpressV1 = ChatModels(
        name = "AmazonTitanTextExpressV1",
        modelName = "amazon.titan-text-express-v1",
        maxTotalTokens = 8192,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.0008,
        outputTokenPricePerK = 0.0016
    )
    val Claude3OpusAWS = ChatModels(
        name = "Claude3OpusAWS",
        modelName = "anthropic.claude-3-opus-20240229-v1:0",
        maxTotalTokens = 200000,
        maxOutTokens = 4096,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 15.0 / 1000.0,
        outputTokenPricePerK = 75.0 / 1000.0
    )
    val CohereCommandTextV14 = ChatModels(
        name = "CohereCommandTextV14",
        modelName = "cohere.command-text-v14",
        maxTotalTokens = 4000,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.0015,
        outputTokenPricePerK = 0.002
    )
    val AI21J2UltraV1 = ChatModels(
        name = "AI21J2UltraV1",
        modelName = "ai21.j2-ultra-v1",
        maxTotalTokens = 8191,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.0125,
        outputTokenPricePerK = 0.0125
    )
    val AI21J2MidV1 = ChatModels(
        name = "AI21J2MidV1",
        modelName = "ai21.j2-mid-v1",
        maxTotalTokens = 8191,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.0188,
        outputTokenPricePerK = 0.0188
    )
    val Claude35Sonnet = ChatModels(
        name = "Claude3Sonnet",
        modelName = "anthropic.claude-3-5-sonnet-20240620-v1:0",
        maxTotalTokens = 200000,
        maxOutTokens = 4096,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.003,
        outputTokenPricePerK = 0.015
    )
    val Claude3Sonnet = ChatModels(
        name = "Claude3Sonnet",
        modelName = "anthropic.claude-3-sonnet-20240229-v1:0",
        maxTotalTokens = 200000,
        maxOutTokens = 4096,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.003,
        outputTokenPricePerK = 0.015
    )
    val Claude3Haiku = ChatModels(
        name = "Claude3Haiku",
        modelName = "anthropic.claude-3-haiku-20240307-v1:0",
        maxTotalTokens = 200000,
        maxOutTokens = 4096,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.00025,
        outputTokenPricePerK = 0.000125
    )
    val ClaudeV2_1 = ChatModels(
        name = "ClaudeV2",
        modelName = "anthropic.claude-v2:1",
        maxTotalTokens = 100000,
        maxOutTokens = 4096,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.008,
        outputTokenPricePerK = 0.024
    )
    val ClaudeV2 = ChatModels(
        name = "ClaudeV2",
        modelName = "anthropic.claude-v2",
        maxTotalTokens = 100000,
        maxOutTokens = 4096,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.008,
        outputTokenPricePerK = 0.024
    )
    val ClaudeV2Instant = ChatModels(
        name = "ClaudeV2",
        modelName = "anthropic.claude-instant-v1",
        maxTotalTokens = 100000,
        maxOutTokens = 4096,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.0008,
        outputTokenPricePerK = 0.0024
    )
    val LLaMA38bInstructAWS = ChatModels(
        name = "LLaMA38bInstructAWS",
        modelName = "meta.llama3-8b-instruct-v1:0",
        maxTotalTokens = 8192,
        maxOutTokens = 2048,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val LLaMA370bInstructAWS = ChatModels(
        name = "LLaMA370bInstructAWS",
        modelName = "meta.llama3-70b-instruct-v1:0",
        maxTotalTokens = 8192,
        maxOutTokens = 2048,
        provider = APIProvider.AWS,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
}