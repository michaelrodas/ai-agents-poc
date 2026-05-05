package org.example

import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage

// 1. Define an interface representing your AI Agent (Assistant)
interface Assistant {
    @SystemMessage("You are a polite, helpful and concise AI assistant.")
    fun chat(message: String): String
}

fun main() {
    // 2. Obtain your API key from environment variables
    val apiKey = System.getenv("OPENAI_API_KEY") ?: "demo"
    
    // 3. Initialize the ChatModel
    // We are using OpenAI here, but LangChain4j supports many others (LocalAI, HuggingFace, etc.)
    val model = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName("gpt-4o-mini") // or "gpt-3.5-turbo", "gpt-4", etc.
        .build()

    // 4. Create the agent using AiServices
    // This dynamically implements the Assistant interface and wires the model and memory
    val assistant = AiServices.builder(Assistant::class.java)
        .chatLanguageModel(model)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .build()

    println("=== AI Agent POC ===")
    println("Using API Key: ${if (apiKey == "demo") "demo (might fail if rate limited)" else "provided via OPENAI_API_KEY"}")
    println("---------------------")
    
    // 5. Interact with the agent
    val userMessage = "Hello, can you explain what LangChain4j is in one sentence?"
    println("User: $userMessage")
    
    try {
        val response = assistant.chat(userMessage)
        println("Agent: $response")
    } catch (e: Exception) {
        println("Error connecting to the LLM: ${e.message}")
        println("Make sure you have set the OPENAI_API_KEY environment variable with a valid key.")
    }
}
