package yjh.cstar.member.application.port

import yjh.cstar.member.domain.Member

interface MemberRepository {
    fun findById(id: Long): Member?
    fun findByEmail(email: String): Member?
    fun findByIdIn(players: List<Long>): List<Member>
    fun save(member: Member): Member
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}
