package yjh.cstar.member.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("[Domain 테스트] Member")
class MemberTest {

    @Test
    fun `회원 생성 테스트`() {
        // given
        val command = MemberCreateCommand(
            email = "email@naver.com",
            password = "password",
            nickname = "nickname"
        )

        // when
        val member = assertDoesNotThrow { Member.create(command) }

        // then
        assertNotNull(member)
        assertEquals("email@naver.com", member.email)
        assertEquals("nickname", member.nickname)
        assertEquals("password", member.password)
    }
}
