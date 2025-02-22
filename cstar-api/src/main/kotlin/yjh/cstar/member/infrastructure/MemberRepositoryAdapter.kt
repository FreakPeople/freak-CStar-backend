package yjh.cstar.member.infrastructure

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.cstar.common.util.logging2.LogTrace
import yjh.cstar.common.util.logging2.TraceStartInfo
import yjh.cstar.member.application.port.MemberRepository
import yjh.cstar.member.domain.Member
import yjh.cstar.member.infrastructure.jpa.MemberEntity
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository

@Repository
class MemberRepositoryAdapter(
    private val trace: LogTrace,
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {
    lateinit var traceStartInfo: TraceStartInfo

    private val logger: Logger = LoggerFactory.getLogger(MemberRepositoryAdapter::class.java)

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
        try {
            traceStartInfo = trace.begin("MemberRepository.create()", logger)

            val result = memberJpaRepository.save(MemberEntity.from(member)).toModel()

            trace.end(traceStartInfo, logger)

            return result
        } catch (e: Exception) {
            trace.exception(traceStartInfo, e, logger)
            throw e
        }
    }

    override fun existsByEmail(email: String): Boolean {
        return memberJpaRepository.existsByEmail(email)
    }

    override fun existsByNickname(nickname: String): Boolean {
        return memberJpaRepository.existsByNickname(nickname)
    }
}
