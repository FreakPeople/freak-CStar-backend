package yjh.cstar.member.application.port

import yjh.cstar.member.domain.Member

interface MemberRepository {
    fun findByEmail(email: String): Member?
    fun save(member: Member): Member
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}
