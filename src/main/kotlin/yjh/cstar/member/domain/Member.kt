package yjh.cstar.member.domain

import java.time.LocalDateTime

class Member(
    val id: Long = 0,
    val email: String,
    val password: String,
    val nickname: String,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun create(command: MemberCreateCommand): Member {
            return Member(
                email = command.email,
                password = command.password,
                nickname = command.nickname
            )
        }
    }
}
