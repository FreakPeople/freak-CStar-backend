package yjh.cstar.member.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import yjh.cstar.common.BaseException
import kotlin.test.assertEquals

@DisplayName("[Domain 테스트] MemberCreateCommand")
class MemberCreateCommandTest {

    @Test
    fun `회원 생성 커맨드 테스트`() {
        assertDoesNotThrow {
            MemberCreateCommand(
                email = "email@naver.com",
                password = "password",
                nickname = "nickname"
            )
        }
    }

    @Test
    fun `비밀 번호 범위 초과시 예외 발생`() {
        // given
        val invalidPasswords = listOf("a".repeat(4), "a".repeat(16))
        val email = "email@naver.com"
        val nickname = "nickname"

        // when, then
        invalidPasswords.forEach { invalidPassword ->
            val exception = assertThrows<BaseException> {
                MemberCreateCommand(
                    email = email,
                    password = invalidPassword,
                    nickname = nickname
                )
            }
            assertEquals(HttpStatus.BAD_REQUEST, exception.baseErrorCode.httpStatus)
        }
    }

    @Test
    fun `닉네임 범위 초과시 예외 발생`() {
        // given
        val invalidNicknames = listOf("a", "a".repeat(16))
        val email = "email@naver.com"
        val password = "password"

        // when, then
        invalidNicknames.forEach { invalidNickname ->
            val exception = assertThrows<BaseException> {
                MemberCreateCommand(
                    email = email,
                    password = password,
                    nickname = invalidNickname
                )
            }
            assertEquals(HttpStatus.BAD_REQUEST, exception.baseErrorCode.httpStatus)
        }
    }
}
