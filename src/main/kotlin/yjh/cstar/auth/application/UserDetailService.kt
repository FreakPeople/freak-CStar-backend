package yjh.cstar.auth.application

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.member.domain.Member
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository

@Component
class UserDetailService(
    private val memberJpaRepository: MemberJpaRepository,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberJpaRepository.findByEmail(email)?.toModel()
            ?: throw BaseException(BaseErrorCode.NOT_FOUND_MEMBER)
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
