package com.simiacryptus.jopenai.models

enum class APIProvider(val base: String? = null) {
  OpenAI("https://api.openai.com/v1"),
  Groq("https://api.groq.com/openai/v1"),
  Perplexity("https://api.perplexity.ai"),
  ModelsLab("https://modelslab.com/api/v6"),
  AWS("https://api.openai.aws"),
}