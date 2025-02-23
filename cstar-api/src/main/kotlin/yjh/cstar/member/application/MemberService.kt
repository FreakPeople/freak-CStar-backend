package yjh.cstar.member.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.aop.annotation.Logging
import yjh.cstar.common.exception.BaseException
import yjh.cstar.member.application.port.MemberRepository
import yjh.cstar.member.application.port.PasswordEncryptor
import yjh.cstar.member.domain.Member
import yjh.cstar.member.domain.MemberCreateCommand

@Transactional(readOnly = true)
@Logging
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncryptor: PasswordEncryptor,
) {
    fun retrieve(email: String) = memberRepository.findByEmail(email)
        ?: throw BaseException(ApiErrorCode.NOT_FOUND_MEMBER)

    fun retrieveMe(myId: Long) = memberRepository.findById(myId)
        ?: throw BaseException(ApiErrorCode.NOT_FOUND_MEMBER)

    fun retrieveAll(playerIds: List<Long>) = memberRepository.findByIdIn(playerIds)

    @Transactional
    fun create(command: MemberCreateCommand): Long {
        checkEmailDuplicated(command.email)
        checkNicknameDuplicated(command.nickname)

        val encodedPassword = passwordEncryptor.encode(command.password)

        val member = Member.create(command, encodedPassword)
        val savedMember = memberRepository.save(member)

        return savedMember.id
    }

    private fun checkNicknameDuplicated(nickname: String) {
        if (memberRepository.existsByNickname(nickname)) {
            throw BaseException(ApiErrorCode.CONFLICT_MEMBER)
        }
    }

    private fun checkEmailDuplicated(email: String) {
        if (memberRepository.existsByEmail(email)) {
            throw BaseException(ApiErrorCode.CONFLICT_MEMBER)
        }
    }
}
