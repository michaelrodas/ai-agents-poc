package org.example

import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage

// 1. Define an interface representing your AI Agent (Assistant)
interface OllamaAssistant {
    @SystemMessage("You are a helpful and concise AI assistant powered by a local model.")
    fun chat(message: String): String
}

fun main() {
    // 2. Initialize the Ollama ChatModel
    // Ensure you have Ollama installed and a model pulled (e.g., `ollama run llama3.2`)
    val model = OllamaChatModel.builder()
        .baseUrl("http://localhost:11434")
        .modelName("llama3.2") // Or whichever model you have pulled in Ollama (e.g. "phi3", "mistral")
        .temperature(0.3)
        .build()

    // 3. Create the agent using AiServices
    val assistant = AiServices.builder(OllamaAssistant::class.java)
        .chatModel(model)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .build()

    println("=== Local AI Agent POC (Ollama) ===")
    println("Ensure Ollama is running on http://localhost:11434 with 'llama3.2' installed.")
    println("---------------------")
    
    // 4. Interact with the agent
    val userMessage = "Hello, can you explain what LangChain is in one sentence?"
    println("User: $userMessage")
    
    try {
        val response = assistant.chat(userMessage)
        println("Agent: $response")
    } catch (e: Exception) {
        println("Error connecting to Ollama: ${e.message}")
        println("Is Ollama running? Did you pull the model? Try running: 'ollama run llama3.2' in your terminal.")
    }
}
