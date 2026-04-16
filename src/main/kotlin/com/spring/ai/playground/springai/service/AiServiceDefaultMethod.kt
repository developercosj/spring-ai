package com.spring.ai.playground.springai.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AiServiceDefaultMethod(private val chatClientBuilder: ChatClient.Builder) {

    private val chatClient: ChatClient = chatClientBuilder.defaultSystem("적절한 감탄사, 웃음등을 넣어서 친절하게 대화를 해주세요.")
        .defaultOptions(ChatOptions.builder().temperature(1.0).maxTokens(300).build()).build()


    fun defaultMethod(question: String): Flux<String> {
        val response = this.chatClient.prompt()
            .user(question)
            .stream()
            .content()
        return response
    }













}