package yjh.cstar.quiz.presentation

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.common.Response
import yjh.cstar.quiz.application.QuizService
import yjh.cstar.quiz.presentation.request.QuizCreateRequest
import yjh.cstar.quiz.presentation.request.toCommand

@RestController
@RequestMapping("/v1")
class QuizController(
    private val quizService: QuizService,
    private val tokenProvider: TokenProvider,
) {

    @PostMapping("/quizzes")
    fun create(
        @RequestBody request: QuizCreateRequest,
        authentication: Authentication,
    ): ResponseEntity<Response<Long>> {
        val memberId = tokenProvider.getMemberId(authentication)
        return ResponseEntity.ok(Response(data = quizService.create(request.toCommand(), memberId)))
    }
}
