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
import yjh.cstar.room.domain.RoomStatus
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJoinEntity
import yjh.cstar.room.infrastructure.jpa.RoomJoinJpaRepository
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository
import java.time.LocalDateTime
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

    @Autowired
    private lateinit var roomJpaRepository: RoomJpaRepository

    @Autowired
    private lateinit var roomJoinJpaRepository: RoomJoinJpaRepository

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

    @DisplayName("Room Entity 연결 테스트")
    @Test
    fun test2() {
        // given
        roomJpaRepository.save(
            RoomEntity(
                maxCapacity = 5,
                currCapacity = 3,
                status = RoomStatus.WAITING,
                createdAt = null,
                updatedAt = null
            )
        )

        // when
        val rooms = roomJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(rooms) },
            { assertEquals(1, rooms.size) }
        )
    }

    @DisplayName("RoomJoin Entity 연결 테스트")
    @Test
    fun test3() {
        // given
        roomJoinJpaRepository.save(
            RoomJoinEntity(
                roomId = 1L,
                playerId = 1L,
                joinedAt = LocalDateTime.now()
            )
        )

        // when
        val roomJoins = roomJoinJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(roomJoins) },
            { assertEquals(1, roomJoins.size) }
        )
    }
}
