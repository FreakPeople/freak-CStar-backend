package yjh.cstar.member.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberEntity, Long> {

    fun existsByEmail(email: String): Boolean

    fun existsByNickname(nickname: String): Boolean
}
