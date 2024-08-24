package yjh.cstar.member.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.member.application.port.MemberRepository
import yjh.cstar.member.application.port.PasswordEncryptor
import yjh.cstar.member.domain.Member
import yjh.cstar.member.domain.MemberCreateCommand

@Transactional(readOnly = true)
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncryptor: PasswordEncryptor,
) {

    fun retrieve(email: String): Member {
        return memberRepository.findByEmail(email) ?: throw BaseException(BaseErrorCode.NOT_FOUND_MEMBER)
    }

    @Transactional
    fun create(command: MemberCreateCommand): Long {
        checkEmailDuplicated(command.email)
        checkNicknameDuplicated(command.nickname)

        val encodedPassword = passwordEncryptor.encode(command.password)

        val member = Member.create(command, encodedPassword)

        return memberRepository.save(member).id
    }

    private fun checkNicknameDuplicated(nickname: String) {
        if (memberRepository.existsByNickname(nickname)) {
            throw BaseException(BaseErrorCode.CONFLICT_MEMBER)
        }
    }

    private fun checkEmailDuplicated(email: String) {
        if (memberRepository.existsByEmail(email)) {
            throw BaseException(BaseErrorCode.CONFLICT_MEMBER)
        }
    }
}
