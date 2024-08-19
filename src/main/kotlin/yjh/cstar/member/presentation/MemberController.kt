package yjh.cstar.member.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import yjh.cstar.common.Response
import yjh.cstar.member.application.MemberService
import yjh.cstar.member.presentation.request.MemberCreateRequest
import yjh.cstar.member.presentation.request.toCommand

@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping("/members")
    fun create(
        @RequestBody request: MemberCreateRequest,
    ): ResponseEntity<Response<Long>> {
        return ResponseEntity.ok(Response(data = memberService.create(request.toCommand())))
    }
}
