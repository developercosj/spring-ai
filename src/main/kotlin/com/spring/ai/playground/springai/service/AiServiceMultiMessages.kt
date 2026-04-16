package com.spring.ai.playground.springai.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.stereotype.Service
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatResponse

@Service
class AiServiceMultiMessages(private val chatClientBuilder: ChatClient.Builder) {

    private val chatClient: ChatClient = chatClientBuilder.build()


    fun multiMessages(question: String, chatMemory: MutableList<Message>): String {
        val systemMessage: SystemMessage = SystemMessage.builder().text("당신은 AI 비서입니다.\n" +
                "    제공되는 지난 대화 내용을 보고 우선적으로 답변해주세요.\n").build()
        if (chatMemory.isEmpty()) {
            chatMemory.add(systemMessage)
        }

        val chatResponse: ChatResponse? = this.chatClient.prompt().messages(chatMemory).user(question).call().chatResponse()
        val userMessage: UserMessage = UserMessage.builder().text(question).build()
        chatMemory.add(userMessage)
        val assistantMessage: AssistantMessage? = chatResponse?.result?.output
        assistantMessage?.let {
            chatMemory.add(it)
        }
        val text: String? = assistantMessage?.text
        return text ?: ""
    }



}