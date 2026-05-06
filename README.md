# AI Agents POC (Kotlin + LangChain4j)

This project is a Proof of Concept (POC) demonstrating how to build and interact with AI Agents using **Kotlin** and **LangChain4j**.

It provides basic examples of how to set up conversational AI assistants with memory, connecting to Google Gemini and local models via Ollama.

## Features

*   **LangChain4j Integration:** Declarative AI agent creation using `AiServices`.
*   **Conversational Memory:** Uses `MessageWindowChatMemory` to remember past interactions.
*   **Multiple Providers:** Examples included connecting to Google Gemini and local LLMs (via Ollama).

## Prerequisites

*   JDK 8 or higher (Java 17+ recommended)
*   Maven
*   **For Google Gemini:** An API key from [Google AI Studio](https://aistudio.google.com/app/apikey).
*   **For Local Models:** Install [Ollama](https://ollama.com/) on your machine.

## Setup & Running

1.  **Clone the repository** and open the project in IntelliJ IDEA / Android Studio.
2.  **Reload Maven Project:** Make sure to sync/reload the Maven project so that all `dev.langchain4j` dependencies are downloaded.

### Running Gemini
1.  Set the environment variable `GEMINI_API_KEY`.
2.  Run `src/main/kotlin/GeminiExample.kt`.

### Running Local Models (Ollama)
1. Ensure Ollama is installed and running.
2. Open your terminal and pull a model (e.g., `ollama run llama3.2`).
3. Run `src/main/kotlin/OllamaExample.kt`.

## Dependencies

This project relies on:
*   `dev.langchain4j:langchain4j` - Core library
*   `dev.langchain4j:langchain4j-google-ai-gemini` - Google Gemini integration
*   `dev.langchain4j:langchain4j-ollama` - Ollama integration for local models
*   `org.slf4j:slf4j-simple` - Required for LangChain4j logging
*   Kotlin Standard Library
