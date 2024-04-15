package com.simiacryptus.jopenai.models

enum class APIProvider(val base: String? = null) {
    Google("https://generativelanguage.googleapis.com"),
    OpenAI("https://api.openai.com/v1"),
    Anthropic("https://api.anthropic.com/v1"),
    AWS("https://api.openai.aws"),
    Groq("https://api.groq.com/openai/v1"),
    Perplexity("https://api.perplexity.ai"),
    ModelsLab("https://modelslab.com/api/v6"),
}