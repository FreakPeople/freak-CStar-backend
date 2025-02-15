package yjh.cstar.member.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberJpaRepository : JpaRepository<MemberEntity, Long> {

    fun existsByEmail(email: String): Boolean

    fun existsByNickname(nickname: String): Boolean

    @Query("SELECT m FROM MemberEntity m WHERE m.deletedAt IS NULL AND m.email = :email")
    fun findByEmail(email: String): MemberEntity?
}
