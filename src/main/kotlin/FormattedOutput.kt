package org.example

import dev.langchain4j.agent.tool.Tool
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import dev.langchain4j.service.V

// ── 1. Structured output types ─────────────────────────────────────────────

enum class Sentiment { POSITIVE, NEUTRAL, NEGATIVE }

data class ProductReview(
    val productName: String,
    val rating: Int,           // 1–5
    val sentiment: Sentiment,
    val pros: List<String>,
    val cons: List<String>,
    val summary: String
)

data class OrderSummary(
    val orderId: String,
    val totalItems: Int,
    val estimatedTotal: Double,
    val currency: String
)

// ── 2. Tool definitions ────────────────────────────────────────────────────

class ShopTools {

    @Tool("Looks up a product price in the catalog given its name")
    fun getProductPrice(productName: String): Double {
        // Simulated catalog lookup
        val catalog = mapOf(
            "laptop" to 999.99,
            "headphones" to 149.99,
            "mouse" to 39.99
        )
        return catalog[productName.lowercase()] ?: 0.0
    }

    @Tool("Calculates the total order price for a list of products")
    fun calculateOrderTotal(products: List<String>): OrderSummary {
        val catalog = mapOf("laptop" to 999.99, "headphones" to 149.99, "mouse" to 39.99)
        val total = products.sumOf { catalog[it.lowercase()] ?: 0.0 }
        return OrderSummary(
            orderId = "ORD-${System.currentTimeMillis()}",
            totalItems = products.size,
            estimatedTotal = total,
            currency = "USD"
        )
    }
}

// ── 3. AI Service interface ────────────────────────────────────────────────

interface ShoppingAgent {

    // Returns a structured data class — LangChain4j enforces JSON schema output
    @SystemMessage("""
        You are a product review analyst.
        Extract structured information from the review text provided.
        Be objective and concise.
    """)
    @UserMessage("Analyze this product review: {{review}}")
    fun analyzeReview(@V("review") review: String): ProductReview

    // Uses tools AND returns structured output
    @SystemMessage("""
        You are a shopping assistant. Use the available tools to look up 
        prices and calculate totals. Always return a complete order summary.
    """)
    @UserMessage("I want to buy the following products: {{products}}")
    fun buildOrder(@V("products") products: String): OrderSummary

    // Plain chat with memory — returns String
    @SystemMessage("You are a friendly shopping assistant. Keep answers brief.")
    fun chat(@UserMessage message: String): String
}

// ── 4. Wire it up and run ──────────────────────────────────────────────────

fun main() {
    val model = GoogleAiGeminiChatModel.builder()
        .apiKey(System.getenv("GEMINI_API_KEY"))
        .modelName("gemini-2.0-flash")
        .temperature(0.3)   // lower = more deterministic, better for structured output
        .build()

    val agent: ShoppingAgent = AiServices.builder(ShoppingAgent::class.java)
        .chatModel(model)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .tools(ShopTools())
        .build()

    // ── Structured output: data extraction ────────────────────────────────
    val review = """
        The Sony WH-1000XM5 headphones are absolutely fantastic! 
        Best noise cancellation I've ever experienced. Battery lasts forever. 
        However, the ear cups feel a bit tight after 2 hours. Paid $349 but 
        worth every penny. 5 stars.
    """.trimIndent()

    val analyzed: ProductReview = agent.analyzeReview(review)
    println("=== Review Analysis ===")
    println("Product : ${analyzed.productName}")
    println("Rating  : ${analyzed.rating}/5")
    println("Sentiment: ${analyzed.sentiment}")
    println("Pros    : ${analyzed.pros.joinToString()}")
    println("Cons    : ${analyzed.cons.joinToString()}")
    println("Summary : ${analyzed.summary}")

    // ── Structured output + tool calling ──────────────────────────────────
    val order: OrderSummary = agent.buildOrder("a laptop and two mice")
    println("\n=== Order Summary ===")
    println("Order ID : ${order.orderId}")
    println("Items    : ${order.totalItems}")
    println("Total    : ${order.currency} ${order.estimatedTotal}")

    // ── Plain chat with memory ─────────────────────────────────────────────
    println("\n=== Chat ===")
    println(agent.chat("Do you have gaming laptops?"))
    println(agent.chat("What was the order total we just calculated?"))  // tests memory
}