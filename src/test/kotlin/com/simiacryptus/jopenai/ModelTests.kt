package com.simiacryptus.jopenai

import com.simiacryptus.jopenai.models.APIProvider
import com.simiacryptus.jopenai.models.ChatModels
import com.simiacryptus.jopenai.util.ClientUtil
import com.simiacryptus.jopenai.util.ClientUtil.toContentList
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModelTests {

  @TestFactory
  fun generateChatModelTests(): Array<DynamicNode> {
    // Retrieve all ChatModels
    val chatModels = ChatModels.values().values

    // Generate a dynamic test for each model
    return chatModels.map { model ->
      DynamicTest.dynamicTest(model.modelName) {
        testChatWithModel(model)
      }
    }.map { it as DynamicNode }.toTypedArray()
  }

  private fun testChatWithModel(model: ChatModels) {
    val prov = ClientUtil.keyMap[ClientUtil.defaultApiProvider.name] ?: return
    if (prov.isBlank()) return
    val client = OpenAIClient(ClientUtil.keyMap.mapKeys { APIProvider.valueOf(it.key) })
    val request = ApiModel.ChatRequest(
      model = model.modelName,
      messages = ArrayList(
        listOf(
          ApiModel.ChatMessage(ApiModel.Role.system, "You are a spiritual teacher".toContentList()),
          ApiModel.ChatMessage(ApiModel.Role.user, "What is the meaning of life?".toContentList()),
        )
      )
    )
    val chatResponse = client.chat(request, model)
    println(chatResponse.choices.first().message?.content ?: "No response")
  }

}