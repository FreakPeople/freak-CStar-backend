package yjh.cstar.auth.application

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.aop.annotation.Logging
import yjh.cstar.common.exception.BaseException
import yjh.cstar.member.domain.Member
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository

@Logging
@Component
class UserDetailService(
    private val memberJpaRepository: MemberJpaRepository,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberJpaRepository.findByEmail(email)?.toModel()
            ?: throw BaseException(ApiErrorCode.NOT_FOUND_MEMBER)
        return createUser(member)
    }

    private fun createUser(member: Member): User {
        return User(
            member.email,
            member.password,
            listOf()
        )
    }
}
