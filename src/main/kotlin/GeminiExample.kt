package org.example

import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage

// 1. Define an interface representing your AI Agent (Assistant)
interface GeminiAssistant {
    @SystemMessage("You are a polite, helpful and concise AI assistant powered by Gemini.")
    fun chat(message: String): String
}

fun main() {
    // 2. Obtain your API key from environment variables
    val apiKey = System.getenv("GEMINI_API_KEY")

    if (apiKey.isNullOrBlank()) {
        println("ERROR: GEMINI_API_KEY environment variable is missing.")
        println("Please set it to your Google AI Studio API key and try again.")
        return
    }

    // 3. Initialize the Gemini ChatModel
    val model = GoogleAiGeminiChatModel.builder()
        .apiKey(apiKey)
        .modelName("gemini-1.5-flash") // You can also use "gemini-1.5-pro"
        .build()

    // 4. Create the agent using AiServices
    val assistant = AiServices.builder(GeminiAssistant::class.java)
        .chatLanguageModel(model)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .build()

    println("=== Gemini AI Agent POC ===")
    println("Using API Key provided via GEMINI_API_KEY")
    println("---------------------")
    
    // 5. Interact with the agent
    val userMessage = "Hello Gemini, can you explain what LangChain4j is in one sentence?"
    println("User: $userMessage")
    
    try {
        val response = assistant.chat(userMessage)
        println("Agent: $response")
    } catch (e: Exception) {
        println("Error connecting to Gemini: ${e.message}")
    }
}
