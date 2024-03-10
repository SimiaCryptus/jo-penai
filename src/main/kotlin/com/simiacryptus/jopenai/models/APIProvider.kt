package com.simiacryptus.jopenai.models

enum class APIProvider(val base: String) {
  OpenAI("https://api.openai.com/v1"),
  Groq("https://api.groq.com/openai/v1"),
  Perplexity("https://api.perplexity.ai"),
  ModelsLab("https://modelslab.com/api/v6"),
}