package yjh.cstar.room.application

import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import yjh.cstar.IntegrationTest
import yjh.cstar.member.infrastructure.jpa.MemberEntity
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository
import yjh.cstar.room.domain.RoomCreateCommand
import yjh.cstar.room.domain.RoomStatus
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DisplayName("[Application 테스트] RoomService")
class RoomServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var roomService: RoomService

    @Autowired
    private lateinit var roomJpaRepository: RoomJpaRepository

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Test
    fun `게임 방 생성 테스트`() {
        // given
        val command = RoomCreateCommand(maxCapacity = 5)

        // when
        val roomId = roomService.create(command)

        // then
        assertTrue(roomId > 0L)
        val room = roomJpaRepository.findByIdOrNull(roomId)?.toModel()

        assertNotNull(room)
        assertEquals(5, room.maxCapacity)
        assertEquals(0, room.currCapacity)
        assertEquals(RoomStatus.WAITING, room.status)
        assertNotNull(room.createdAt)
        assertNotNull(room.updatedAt)
        assertNull(room.deletedAt)
    }

    @Test
    fun `게임 방 참가 요청 테스트`() {
        // given
        val roomId = roomJpaRepository.save(
            RoomEntity(
                maxCapacity = 5,
                currCapacity = 3,
                status = RoomStatus.WAITING,
                createdAt = null,
                updatedAt = null
            )
        ).toModel().id

        val memberId = memberJpaRepository.save(
            MemberEntity(
                email = "test@test.com",
                password = "12345",
                nickname = "testNickname",
                createdAt = null,
                updatedAt = null
            )
        ).toModel().id

        // when
        val createdRoomId = roomService.join(roomId, memberId)

        // then
        assertTrue(createdRoomId > 0L)
        val room = roomJpaRepository.findByIdOrNull(createdRoomId)?.toModel()

        assertNotNull(room)
        assertEquals(4, room.currCapacity)
    }
}
