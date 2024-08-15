package yjh.cstar.jpa

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import yjh.cstar.config.JpaConfig
import yjh.cstar.member.infrastructure.jpa.MemberEntity
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ActiveProfiles("local-test")
@Import(JpaConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = ["file:src/main/resources/db/init_schema.sql"])
@DisplayName("JPA 연결 테스트")
@DataJpaTest
class JpaTest {

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @DisplayName("Member Entity 연결 테스트")
    @Test
    fun test1() {
        // given
        memberJpaRepository.save(
            MemberEntity(
                email = "email",
                password = "password",
                nickname = "nickname",
                createdAt = null,
                updatedAt = null
            )
        )

        // when
        val members = memberJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(members) },
            { assertEquals(1, members.size) }
        )
    }
}
