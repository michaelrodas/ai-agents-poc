package org.example

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.models.responses.Response
import com.openai.models.responses.ResponseCreateParams

object HelloWorld {
    @JvmStatic
    fun main(args: Array<String>) {
        val client: OpenAIClient = OpenAIOkHttpClient.fromEnv()

        val params: ResponseCreateParams = ResponseCreateParams.builder()
            .input("Say this is a test")
            .model("gpt-5.5")
            .build()

        val response: Response = client.responses().create(params)
        System.out.println(response)
    }
}