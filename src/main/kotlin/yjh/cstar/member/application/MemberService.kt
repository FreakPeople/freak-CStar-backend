package yjh.cstar.member.application

import org.springframework.stereotype.Service
import yjh.cstar.member.application.port.MemberRepository
import yjh.cstar.member.domain.Member
import yjh.cstar.member.domain.MemberCreateCommand

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

    fun create(command: MemberCreateCommand): Long {
        val member = Member.create(command)
        return memberRepository.save(member).id
    }
}
