package yjh.cstar.room.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import yjh.cstar.common.BaseException
import kotlin.test.assertEquals

@DisplayName("[Domain 테스트] RoomCreateCommand")
class RoomCreateCommandTest {

    @Test
    fun `게임 생성 커맨드 테스트`() {
        // given
        val maxCapacity = 5
        // when, then
        assertDoesNotThrow { RoomCreateCommand(maxCapacity = maxCapacity) }
    }

    @Test
    fun `최대 수용 가능 인원수의 범위 초과시 예외 발생`() {
        // given
        val invalidCapacity = listOf(0, 1, 6, 100)

        // when, then
        invalidCapacity.forEach { maxCapacity ->
            val exception = assertThrows<BaseException> { RoomCreateCommand(maxCapacity = maxCapacity) }
            assertEquals(HttpStatus.BAD_REQUEST, exception.baseErrorCode.httpStatus)
        }
    }
}
