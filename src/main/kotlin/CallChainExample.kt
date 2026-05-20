package org.example

import dev.langchain4j.model.ollama.OllamaChatModel

/**
 * LLM Call Chain Example: 3-step iterative paragraph writing
 *
 * This example demonstrates a call chain where:
 * 1. First call: Generate a draft paragraph about a topic
 * 2. Second call: Critically analyze the draft and suggest improvements
 * 3. Third call: Write the final improved paragraph
 */

fun main() {
    // Initialize the Ollama ChatModel
    val model = OllamaChatModel.builder()
        .baseUrl("http://localhost:11434")
        .modelName("llama3.2")
        .temperature(0.7)
        .build()

    println("=== LLM Call Chain: Iterative Paragraph Writing ===")
    println("Ensure Ollama is running on http://localhost:11434 with 'llama3.2' installed.")
    println("=".repeat(60))
    println()

    // Input topic
    val topic = "the impact of artificial intelligence on modern education"

    try {
        // STEP 1: Generate initial draft
        println("STEP 1: Generating initial draft...")
        println("-".repeat(60))

        val draftPrompt = """
            Write a paragraph (4-5 sentences) about the following topic: $topic

            Focus on being informative and clear. This is just a draft.
        """.trimIndent()

        val draft = model.chat(draftPrompt)
        println("DRAFT:")
        println(draft)
        println()

        // STEP 2: Critical analysis and suggestions
        println("STEP 2: Analyzing draft and suggesting improvements...")
        println("-".repeat(60))

        val analysisPrompt = """
            Review the following paragraph and provide a critical analysis:

            "$draft"

            Provide:
            1. What works well
            2. What could be improved
            3. Specific suggestions for enhancement (structure, clarity, depth, examples)

            Be constructive and specific.
        """.trimIndent()

        val analysis = model.chat(analysisPrompt)
        println("ANALYSIS & SUGGESTIONS:")
        println(analysis)
        println()

        // STEP 3: Write improved final version
        println("STEP 3: Writing improved final paragraph...")
        println("-".repeat(60))

        val finalPrompt = """
            Based on this original draft:
            "$draft"

            And this critical analysis with suggestions:
            "$analysis"

            Write an improved final version of the paragraph about: $topic

            Incorporate the suggestions while maintaining a clear, engaging style.
            Output only the final paragraph, nothing else.
        """.trimIndent()

        val finalParagraph = model.chat(finalPrompt)
        println("FINAL PARAGRAPH:")
        println(finalParagraph)
        println()

        println("=".repeat(60))
        println("Call chain completed successfully!")

    } catch (e: Exception) {
        println("Error: ${e.message}")
        println("Make sure Ollama is running with: docker run -d -p 11434:11434 ollama/ollama")
        println("And the model is pulled: docker exec <container> ollama pull llama3.2")
    }
}
