package com.spring.ai.playground.springai.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux


// Template prompt
@Service
class AiServicePromptTemplate(
    private val chatClientBuilder: ChatClient.Builder
) {
    private val chatClient = chatClientBuilder.build()
    val systemTemplate: PromptTemplate = SystemPromptTemplate.builder().template("답변을 생성할 때 HTML와 CSS를 사용해서 파란 글자로 출력하세요.\n" +
            "<span> 태그 안에 들어갈 내용만 출력하세요.\n").build()
    val userTemplate: PromptTemplate = PromptTemplate.builder().template("다음 한국어 문장을 {language}로 번역해주세요.\n" +
            " 문장: {statement}").build()

    // 유저 템플릿만 추가
    fun promptTemplate1(statement: String, language: String) : Flux<String> {
            val prompt: Prompt = this.userTemplate.create(mapOf("statement" to statement, "language" to language))
            val response: Flux<String> = this.chatClient.prompt(prompt).stream().content()
            return response
    }

    // 시스템 템플릿, 유저 템플릿 모두 추가
    fun promptTemplate2(statement: String, language: String): Flux<String> {
        val systemMessage = systemTemplate.createMessage()
        val userMessage = userTemplate.createMessage(mapOf("statement" to statement, "language" to language))
        return chatClient.prompt()
            .messages(systemMessage, userMessage)
            .stream().content()
    }

    // 시스템 템플릿, 유저 템플릿 추가
    fun promptTemplate3(statement: String, language: String): Flux<String> {
        return chatClient.prompt()
            .system(systemTemplate.render())
            .user(userTemplate.render(mapOf("statement" to statement, "language" to language)))
            .stream().content()
    }

    fun promptTemplate4(statement: String, language: String): Flux<String> {
        val systemText = "답변을 생성할 때 HTML와 CSS를 사용해서 파란 글자로 출력하세요.\n<span> 태그 안에 들어갈 내용만 출력하세요.\n"
        val userText = "다음 한국어 문장을 ${language}로 번역해주세요.\n 문장: ${statement}\n"
        return chatClient.prompt()
            .system(systemText)
            .user(userText)
            .stream().content()
    }
}