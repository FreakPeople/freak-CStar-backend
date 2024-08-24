package yjh.cstar.auth.presentation

import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.auth.presentation.request.LoginRequest
import yjh.cstar.auth.presentation.response.JwtTokenResponse
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.common.Response
import yjh.cstar.member.application.MemberService

@RestController
@RequestMapping("/v1")
class AuthController(
    private val tokenProvider: TokenProvider,
    private val memberService: MemberService,
    private val passwordEncoder: PasswordEncoder,
) {

    @PostMapping("/authenticate")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<Response<JwtTokenResponse>> {
        val (email, inputPassword) = request

        val member = memberService.retrieve(email = email)

        require(passwordEncoder.matches(inputPassword, member.password)) {
            throw BaseException(BaseErrorCode.PASSWORD_INVALID)
        }

        val jwtTokenResponse = JwtTokenResponse(accessToken = tokenProvider.createToken(email))
        return ResponseEntity.ok(Response(data = jwtTokenResponse))
    }
}
