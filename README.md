# AI Agents POC (Kotlin + LangChain4j)

This project is a Proof of Concept (POC) demonstrating how to build and interact with AI Agents using **Kotlin** and **LangChain4j**.

It provides basic examples of how to set up conversational AI assistants with memory, connecting to Google Gemini.

## Features

*   **LangChain4j Integration:** Declarative AI agent creation using `AiServices`.
*   **Conversational Memory:** Uses `MessageWindowChatMemory` to remember past interactions.
*   **Google Gemini Provider:** Example included connecting to Google Gemini.

## Prerequisites

*   JDK 8 or higher (Java 17+ recommended)
*   Maven
*   An API key for Google Gemini:
    *   Get an API key from [Google AI Studio](https://aistudio.google.com/app/apikey).

## Setup & Running

1.  **Clone the repository** and open the project in IntelliJ IDEA / Android Studio.
2.  **Reload Maven Project:** Make sure to sync/reload the Maven project so that all `dev.langchain4j` dependencies are downloaded.
3.  **Set your API Key:**
    *   Set the environment variable `GEMINI_API_KEY`.
4.  **Run the example:**
    *   Run `src/main/kotlin/GeminiExample.kt` to test the Google Gemini integration.

## Dependencies

This project relies on:
*   `dev.langchain4j:langchain4j` - Core library
*   `dev.langchain4j:langchain4j-google-ai-gemini` - Google Gemini integration
*   `org.slf4j:slf4j-simple` - Required for LangChain4j logging
*   Kotlin Standard Library
