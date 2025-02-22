package yjh.cstar.member.application

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.exception.BaseException
import yjh.cstar.common.util.logging2.LogTrace
import yjh.cstar.common.util.logging2.TraceStartInfo
import yjh.cstar.member.application.port.MemberRepository
import yjh.cstar.member.application.port.PasswordEncryptor
import yjh.cstar.member.domain.Member
import yjh.cstar.member.domain.MemberCreateCommand

@Transactional(readOnly = true)
@Service
class MemberService(
    private val trace: LogTrace,
    private val memberRepository: MemberRepository,
    private val passwordEncryptor: PasswordEncryptor,
) {
    lateinit var traceStartInfo: TraceStartInfo

    private val logger: Logger = LoggerFactory.getLogger(MemberService::class.java)

    fun retrieve(email: String) = memberRepository.findByEmail(email)
        ?: throw BaseException(ApiErrorCode.NOT_FOUND_MEMBER)

    fun retrieveMe(myId: Long) = memberRepository.findById(myId)
        ?: throw BaseException(ApiErrorCode.NOT_FOUND_MEMBER)

    fun retrieveAll(playerIds: List<Long>) = memberRepository.findByIdIn(playerIds)

    @Transactional
    fun create(command: MemberCreateCommand): Long {
        try {
            traceStartInfo = trace.begin("MemberService.create()", logger)

            checkEmailDuplicated(command.email)
            checkNicknameDuplicated(command.nickname)

            val encodedPassword = passwordEncryptor.encode(command.password)

            val savedMember = Member.create(command, encodedPassword)
                .let { memberRepository.save(it) }

            val result = savedMember.id
            trace.end(traceStartInfo, logger)

            return result
        } catch (e: Exception) {
            trace.exception(traceStartInfo, e, logger)
            throw e
        }
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
