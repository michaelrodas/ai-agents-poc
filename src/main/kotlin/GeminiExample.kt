package org.example

import dev.langchain4j.agent.tool.Tool
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage

// 1. Define an interface representing your AI Agent (Assistant)
interface GeminiAssistant {
    @SystemMessage("You are a polite, helpful and concise AI assistant powered by Gemini.")
    fun chat(message: String): String
}

// Define tools the agent can call
class CalculatorTools {

    @Tool("Adds two numbers together")
    fun add(a: Double, b: Double): Double = a + b

    @Tool("Multiplies two numbers together")
    fun multiply(a: Double, b: Double): Double = a * b
}

class MoviesTools {
    data class Movie(
        val title: String,
        val director: String,
        val year: Int,
        val rating: Double
    )

    val movieDatabase: Map<String, Movie> = mapOf(
        "inception" to Movie(
            title = "Inception",
            director = "Christopher Nolan",
            year = 2010,
            rating = 8.8
        ),
        "interstellar" to Movie(
            title = "Interstellar",
            director = "Christopher Nolan",
            year = 2014,
            rating = 8.7
        ),
        "dark_knight" to Movie(
            title = "The Dark Knight",
            director = "Christopher Nolan",
            year = 2008,
            rating = 9.0
        ),
        "prestige" to Movie(
            title = "The Prestige",
            director = "Christopher Nolan",
            year = 2006,
            rating = 8.5
        ),
        "dunkirk" to Movie(
            title = "Dunkirk",
            director = "Christopher Nolan",
            year = 2017,
            rating = 7.8
        )
    )

    @Tool("Find all movies by a specific director")
    fun lookUpMovieByDirector(director: String): String {
        """
            Find all movies by a specific director.
            
            Args: 
                director: the name of the director to search for
                
            Returns:
                A formatted string listing all movies by that director, or a message if none are found.            
        """.trimIndent()

        val moviesByDirector = movieDatabase.values
            .filter { it.director == director }

        return if (moviesByDirector.isEmpty()) {
            "No movies found by director $director"
        } else
            "Movies by $director: " + moviesByDirector.joinToString(",") { "${it.title}-${it.year}" }

    }
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
    // Use gemini-1.5-flash as the model name
    val model = GoogleAiGeminiChatModel.builder()
        .apiKey(apiKey)
        .modelName("gemini-2.5-flash")
        .temperature(0.7)
        .build()

    val memory = MessageWindowChatMemory.withMaxMessages(10)

    // 4. Create the agent using AiServices
    val assistant = AiServices.builder(GeminiAssistant::class.java)
        .chatModel(model)
        .chatMemory(memory)
        .tools(CalculatorTools(), MoviesTools())
        .build()

    println("=== Gemini AI Agent POC ===")
    println("Using API Key provided via GEMINI_API_KEY")
    println("---------------------")
    
    // 5. Interact with the agent
    val director = "Christofer Nolan" // Don Ramon
    val userMessage = "What movies has $director directed?" //"Hello Gemini, can you explain what LangChain4j is in one sentence?"
    println("User: $userMessage")
    
    try {
        val response = assistant.chat(userMessage)
        println("Agent: $response")
    } catch (e: Exception) {
        println("Error connecting to Gemini: ${e.message}")
    }

//    println(assistant.chat("What is 42 multiplied by 7?"))
//    println(assistant.chat("Now add 100 to that result."))
//    println(assistant.chat("What were the two calculations you just did?"))
}
