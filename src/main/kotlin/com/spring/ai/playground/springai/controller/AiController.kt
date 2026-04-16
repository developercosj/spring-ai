package com.spring.ai.playground.springai.controller

import com.spring.ai.playground.springai.service.AiService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/ai")
class AiController(
    private val aiService: AiService
) {


    @PostMapping("/chat-model")
    fun chatModel(@RequestParam("question") question: String): String {
        val answerText = aiService.generateText(question)
        return answerText
    }



    // APPLICATION_NDJSON_VALUE : 라인으로 구분된 청크 텍스트
    @PostMapping("/chat-model-stream", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun chatModelStream(@RequestParam("question") question: String): Flux<String> {
        val answerStreamText = aiService.generateStreamText(question)
        return answerStreamText
    }



}