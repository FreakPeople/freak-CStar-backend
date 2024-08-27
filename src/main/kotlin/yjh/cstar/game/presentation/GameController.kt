package yjh.cstar.game.presentation

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.common.Response
import yjh.cstar.game.application.GameService
import yjh.cstar.game.presentation.request.GameStartRequest
import yjh.cstar.game.presentation.request.toCommand

@RestController
@RequestMapping("/v1")
class GameController(
    private val gameService: GameService,
    private val tokenProvider: TokenProvider,
) {
    @PostMapping("/games")
    fun start(
        @RequestBody request: GameStartRequest,
        authentication: Authentication,
    ): ResponseEntity<Response<Long>> {
        val playerId = tokenProvider.getMemberId(authentication)
        gameService.start(request.toCommand())
        return ResponseEntity.ok(Response())
    }
}
