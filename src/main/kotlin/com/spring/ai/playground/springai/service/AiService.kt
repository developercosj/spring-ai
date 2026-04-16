package com.spring.ai.playground.springai.service

import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AiService(
    private val chatModel: ChatModel,
) {

    fun generateText(question: String): String {
        // 시스템 메시지 생성
        val systemMessage: SystemMessage = SystemMessage.builder()
            .text("사용자 질문에 대해 한국어로 답변을 해야 합니다.")
            .build()

        // 사용자 메시지 생성
        val userMessage = UserMessage.builder()
            .text(question)
            .build()

        // 대화 옵션 설정
        val chatOptions: ChatOptions = ChatOptions.builder()
            .model("gpt-4o-mini")
            .temperature(0.3) // LLM 응답 창의성(다양성)을 조절하는 하이퍼파라미터 낮은 값일수록 일관성 있고 예측 가능한 답변 생성, 높은 값일수록 더 창의적이고 예상치 못한 답변 생성 (범위 : 0.0 ~ 1.0)
            .maxTokens(1000) // LLM 의 과도한 응답 생성을 줄이기 위해 응답 최대 토큰수 고정 가능
            .build()

        val prompt: Prompt = Prompt.builder()
            .messages(systemMessage, userMessage)
            .chatOptions(chatOptions)
            .build()

        // call() 메소드는 동기 방식으로 요청하기 때문에 답변이 올때까지 블로킹 처리가 된다.
        // 응답이 오면 ChatResponse 로 반환
        val chatResponse = chatModel.call(prompt)
        val assistantMessage = chatResponse.result.output
        val answer: String = assistantMessage.text.toString()

        return answer


    }

    fun generateStreamText(question: String): Flux<String> {
        // 시스템 메시지 생성
        val systemMessage: SystemMessage = SystemMessage.builder()
            .text("사용자 질문에 대해 한국어로 답변을 해야 합니다.")
            .build()

        // 사용자 메시지 생성
        val userMessage = UserMessage.builder()
            .text(question)
            .build()

        // 대화 옵션 설정
        val chatOptions: ChatOptions = ChatOptions.builder()
            .model("gpt-4o")
            .temperature(0.3) // LLM 응답 창의성(다양성)을 조절하는 하이퍼파라미터 낮은 값일수록 일관성 있고 예측 가능한 답변 생성, 높은 값일수록 더 창의적이고 예상치 못한 답변 생성 (범위 : 0.0 ~ 1.0)
            .maxTokens(1000) // LLM 의 과도한 응답 생성을 줄이기 위해 응답 최대 토큰수 고정 가능
            .build()

        val prompt: Prompt = Prompt.builder()
            .messages(systemMessage, userMessage)
            .chatOptions(chatOptions)
            .build()

        // LLM에게 요청하고 응답받기
        // 비동기 스트림 타입 -> 여러개의 텍스트 청크들이 순차적으로 출력
        val fluxResponse: Flux<ChatResponse> = chatModel.stream(prompt)
        val fluxString: Flux<String> = fluxResponse.map { chatResponse ->
            chatResponse.result.output.text ?: ""
        }

        return fluxString
    }





}