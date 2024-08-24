package yjh.cstar.member.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import yjh.cstar.IntegrationTest
import yjh.cstar.common.BaseException
import yjh.cstar.member.domain.MemberCreateCommand
import yjh.cstar.member.infrastructure.jpa.MemberEntity
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
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
    fun `회원 조회 테스트 - 존재하지 않는 email`() {
        // given
        memberJpaRepository.save(
            MemberEntity(
                email = "email@naver.com",
                password = "password",
                nickname = "nickname",
                createdAt = null,
                updatedAt = null
            )
        )
        val email = "email_2@naver.com"

        // when
        val exception = assertThrows<BaseException> { memberService.retrieve(email) }

        // then
        assertEquals(HttpStatus.NOT_FOUND, exception.baseErrorCode.httpStatus)
    }

    @Test
    fun `회원 조회 테스트 - email로 조회`() {
        // given
        memberJpaRepository.save(
            MemberEntity(
                email = "email@naver.com",
                password = "password",
                nickname = "nickname",
                createdAt = null,
                updatedAt = null
            )
        )
        val email = "email@naver.com"

        // when
        val member = memberService.retrieve(email)

        // then
        assertEquals("email@naver.com", member.email)
        assertEquals("nickname", member.nickname)
    }

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
        assertEquals("nickname", member.nickname)
        assertNotNull(member.createdAt)
        assertNotNull(member.updatedAt)
        assertNull(member.deletedAt)

        assertNotEquals("password", member.password)
    }

    @Test
    fun `회원 생성 시 email이 중복되면 예외를 발생`() {
        // given
        memberJpaRepository.save(
            MemberEntity(
                email = "email@naver.com",
                password = "password_1",
                nickname = "nickname_1",
                createdAt = null,
                updatedAt = null
            )
        )
        val command = MemberCreateCommand(
            email = "email@naver.com",
            password = "password_2",
            nickname = "nickname_2"
        )

        // when
        val exception = assertThrows<BaseException> { memberService.create(command) }

        // then
        assertEquals(HttpStatus.CONFLICT, exception.baseErrorCode.httpStatus)
    }

    @Test
    fun `회원 생성 시 nickname이 중복되면 예외를 발생`() {
        // given
        memberJpaRepository.save(
            MemberEntity(
                email = "email@naver.com_1",
                password = "password_1",
                nickname = "nickname",
                createdAt = null,
                updatedAt = null
            )
        )
        val command = MemberCreateCommand(
            email = "email@naver.com_2",
            password = "password_2",
            nickname = "nickname"
        )

        // when
        val exception = assertThrows<BaseException> { memberService.create(command) }

        // then
        assertEquals(HttpStatus.CONFLICT, exception.baseErrorCode.httpStatus)
    }
}
