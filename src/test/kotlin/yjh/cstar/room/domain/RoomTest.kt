package yjh.cstar.room.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RoomTest {

    @DisplayName("게임방 생성 테스트")
    @Test
    fun test1() {
        // given
        val roomCreateCommand = RoomCreateCommand(maxCapacity = 5)

        // when
        val room = assertDoesNotThrow { Room.create(roomCreateCommand) }

        // then
        assertNotNull(room)
        assertEquals(5, room.maxCapacity)
        assertEquals(0, room.currCapacity)
        assertEquals(RoomStatus.WAITING, room.status)
    }
}
