package yjh.cstar.member.infrastructure

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import yjh.cstar.member.application.port.PasswordEncryptor

@Repository
class PasswordEncryptorAdapter(
    private val passwordEncoder: PasswordEncoder,
) : PasswordEncryptor {
    override fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }

    override fun isMatches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
