package yjh.cstar.quiz.presentation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.common.Response
import yjh.cstar.quiz.application.QuizService
import yjh.cstar.quiz.presentation.request.QuizCreateRequest
import yjh.cstar.quiz.presentation.request.toCommand
import yjh.cstar.quiz.presentation.response.QuizResponse

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

    @GetMapping("/quizzes")
    fun retrieveAllByCategory(
        @RequestParam category: String,
        @PageableDefault(size = 10) pageable: Pageable,
    ): ResponseEntity<Response<Page<QuizResponse>>> {
        val responses = quizService.retrieveAllByCategory(category, pageable).map { QuizResponse.from(it) }
        return ResponseEntity.ok(Response(data = responses))
    }
}
