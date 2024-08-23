package yjh.cstar.room.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import yjh.cstar.common.BaseException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("[Domain 테스트] Room")
class RoomTest {

    @Test
    fun `게임 방 생성 테스트`() {
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

    @Test
    fun `게임 방 참가 성공 테스트`() {
        // given
        val room = Room(
            maxCapacity = 5,
            currCapacity = 3,
            status = RoomStatus.WAITING
        )

        // when
        room.entrance()

        // then
        assertEquals(4, room.currCapacity)
    }

    @Test
    fun `게임 방 참가 실패 테스트 - 인원 초과 `() {
        // given
        val room = Room(
            maxCapacity = 5,
            currCapacity = 5,
            status = RoomStatus.WAITING
        )

        // when & then
        val exception = assertThrows<BaseException> {
            room.entrance()
        }
        assertEquals(HttpStatus.CONFLICT, exception.baseErrorCode.httpStatus)
    }

    @Test
    fun `게임 방 참가 실패 테스트 - 대기상태 아님 `() {
        // given
        val room = Room(
            maxCapacity = 5,
            currCapacity = 3,
            status = RoomStatus.IN_PROGRESS
        )

        // when & then
        val exception = assertThrows<BaseException> {
            room.entrance()
        }
        assertEquals(HttpStatus.CONFLICT, exception.baseErrorCode.httpStatus)
    }
}
