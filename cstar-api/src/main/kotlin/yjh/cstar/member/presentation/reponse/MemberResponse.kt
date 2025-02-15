package yjh.cstar.member.presentation.reponse

import yjh.cstar.member.domain.Member
import java.time.LocalDateTime

data class MemberResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id,
                email = member.email,
                nickname = member.nickname,
                createdAt = member.createdAt,
                updatedAt = member.updatedAt
            )
        }
    }
}
