package yjh.cstar.member.application.port

interface PasswordEncryptor {
    fun encode(password: String): String
    fun isMatches(rawPassword: String, encodedPassword: String): Boolean
}
