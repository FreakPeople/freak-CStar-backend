package yjh.cstar.member.presentation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.common.response.Response
import yjh.cstar.common.util.logging2.LogTrace
import yjh.cstar.common.util.logging2.TraceStartInfo
import yjh.cstar.member.application.MemberService
import yjh.cstar.member.presentation.reponse.MemberResponse
import yjh.cstar.member.presentation.request.MemberCreateRequest
import yjh.cstar.member.presentation.request.toCommand

@RestController
@RequestMapping("/v1")
class MemberController(
    private val trace: LogTrace,
    private val memberService: MemberService,
    private val tokenProvider: TokenProvider,
) {
    private val logger: Logger = LoggerFactory.getLogger(MemberController::class.java)

    lateinit var traceStartInfo: TraceStartInfo

    @PostMapping("/members")
    fun create(
        @RequestBody request: MemberCreateRequest,
    ): ResponseEntity<Response<Long>> {
        try {
            traceStartInfo = trace.begin("MemberController.create()", logger)

            val result = ResponseEntity.ok(Response(data = memberService.create(request.toCommand())))

            trace.end(traceStartInfo, logger)
            return result
        } catch (e: Exception) {
            trace.exception(traceStartInfo, e, logger)
            throw e
        }
    }

    @GetMapping("/members/me")
    fun retrieveMe(
        authentication: Authentication,
    ): ResponseEntity<Response<MemberResponse>> {
        val myId = tokenProvider.getMemberId(authentication)
        val me = memberService.retrieveMe(myId)
        return ResponseEntity.ok(Response(data = MemberResponse.from(me)))
    }
}
