package yjh.cstar.room.application

import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import yjh.cstar.IntegrationTest
import yjh.cstar.room.domain.RoomCreateCommand
import yjh.cstar.room.domain.RoomStatus
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
}
