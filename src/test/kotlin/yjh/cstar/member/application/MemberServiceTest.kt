package yjh.cstar.member.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import yjh.cstar.IntegrationTest
import yjh.cstar.member.domain.MemberCreateCommand
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DisplayName("[Application 테스트] MemberService")
class MemberServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Test
    fun `회원 생성 테스트`() {
        // given
        val command = MemberCreateCommand(
            email = "email@naver.com",
            password = "password",
            nickname = "nickname"
        )

        // when
        val memberId = memberService.create(command)

        // then
        assertTrue(memberId > 0L)
        val member = memberJpaRepository.findByIdOrNull(memberId)?.toModel()

        assertNotNull(member)
        assertEquals("email@naver.com", member.email)
//        assertNotEquals("password", member.password)
        assertEquals("nickname", member.nickname)
        assertNotNull(member.createdAt)
        assertNotNull(member.updatedAt)
        assertNull(member.deletedAt)
    }
}
