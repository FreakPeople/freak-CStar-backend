package yjh.cstar.member.presentation.request

import yjh.cstar.member.domain.MemberCreateCommand

data class MemberCreateRequest(
    val email: String,
    val password: String,
    val nickname: String,
)

fun MemberCreateRequest.toCommand() = MemberCreateCommand(
    email = email,
    password = password,
    nickname = nickname
)
