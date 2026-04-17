package com.spring.ai.playground.springai.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service

@Service
class AiServiceStepBackPrompt(private val chatClientBuilder: ChatClient.Builder) {
    private val chatClient = chatClientBuilder.build()

    fun stepBackPrompt(question: String): String {
        val questions = chatClient.prompt()
            .user(
                """
            사용자 질문을 처리하기 Step-Back 프롬프트 기법을 사용하려고 합니다.
            사용자 질문을 단계별 질문들로 재구성해주세요. 
            맨 마지막 질문은 사용자 질문과 일치해야 합니다.
            단계별 질문을 항목으로 하는 JSON 배열로 출력해 주세요.
            예시: ["...", "...", "...", ...]
            사용자 질문: %s  
            """.trimIndent().format(question))
            .call()
            .content()

        val json: String = questions?.substring(questions.indexOf("["), questions.indexOf("]") + 1) ?: ""

        val objectMapper = ObjectMapper()
        val listQuestion: List<String> = objectMapper.readValue(json, object : TypeReference<List<String>>() {})

        val answerArray = Array(listQuestion.size) { "" }
        for (i in listQuestion.indices) {
            val stepQuestion = listQuestion[i]
            val stepAnswer = getStepAnswer(stepQuestion, *answerArray)
            answerArray[i] = stepAnswer
        }

        return answerArray.last()

    }

    fun getStepAnswer(question: String, vararg prevStepAnswers: String?): String {
        val context = prevStepAnswers.joinToString("") { it ?: "" }
        return chatClient.prompt()
            .user(
                """
            $question // 현재질문 
            문맥: $context // 이전 질문 내역 
            """.trimIndent()
            )
            .call()
            .content() ?: ""
    }






}