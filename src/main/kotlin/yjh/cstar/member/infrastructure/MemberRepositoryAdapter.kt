package yjh.cstar.member.infrastructure

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.cstar.member.application.port.MemberRepository
import yjh.cstar.member.domain.Member
import yjh.cstar.member.infrastructure.jpa.MemberEntity
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository

@Repository
class MemberRepositoryAdapter(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {
    override fun findById(id: Long): Member? {
        return memberJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun findByEmail(email: String): Member? {
        return memberJpaRepository.findByEmail(email)?.toModel()
    }

    override fun findByIdIn(playerIds: List<Long>): List<Member> {
        return memberJpaRepository.findAllById(playerIds).map { it.toModel() }
    }

    override fun save(member: Member): Member {
        return memberJpaRepository.save(MemberEntity.from(member)).toModel()
    }

    override fun existsByEmail(email: String): Boolean {
        return memberJpaRepository.existsByEmail(email)
    }

    override fun existsByNickname(nickname: String): Boolean {
        return memberJpaRepository.existsByNickname(nickname)
    }
}
