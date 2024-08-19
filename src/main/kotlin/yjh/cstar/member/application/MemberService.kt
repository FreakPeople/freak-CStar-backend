package yjh.cstar.member.application

import org.springframework.stereotype.Service
import yjh.cstar.member.application.port.MemberRepository
import yjh.cstar.member.application.port.PasswordEncryptor
import yjh.cstar.member.domain.Member
import yjh.cstar.member.domain.MemberCreateCommand

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncryptor: PasswordEncryptor,
) {

    fun create(command: MemberCreateCommand): Long {
        val encodedPassword = passwordEncryptor.encode(command.password)

        val member = Member.create(command, encodedPassword)

        return memberRepository.save(member).id
    }
}
