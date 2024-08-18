package yjh.cstar.member.infrastructure

import org.springframework.stereotype.Repository
import yjh.cstar.member.application.port.MemberRepository
import yjh.cstar.member.domain.Member
import yjh.cstar.member.infrastructure.jpa.MemberEntity
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository

@Repository
class MemberRepositoryAdapter(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {
    override fun save(member: Member): Member {
        return memberJpaRepository.save(MemberEntity.from(member)).toModel()
    }
}
