package com.spring.ai.playground.springai.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux


@Service
class AiServiceByChatClient(private val chatClientBuilder: ChatClient.Builder) {
    private val chatClient = chatClientBuilder.build()

    fun generateText(question: String): String? {
        val answer: String? = chatClient.prompt()
            .system("사용자 질문에 대해 한국어로 답변을 해야 합니다.") // SystemMessage -> system
            .user(question) // UserMessage -> user
            .options(ChatOptions.builder() // chatOption -> options
                .temperature(0.3)
                .maxTokens(1000)
                .build()
            )
            .call()
            .content() // LLM 의 완전한 답변 텍스트 반환

        return answer
    }


    // ChatClient 는 Fluent API 스타일로 설계되어 있어서 메소드 체이닝을 통해 메소드를 순차적으로 호출가능
    fun generateStreamText(question: String): Flux<String> {
        // LLM  에게 요청 응답
        val fluxString: Flux<String> = chatClient.prompt()
            .system("사용자 질문에 대해 한국어로 답변을 해야 합니다.")
            .user(question)
            .options(ChatOptions.builder()
                .temperature(0.3)
                .maxTokens(1000)
                .build()
            )
            .stream()
            .content()

        return fluxString
    }




}