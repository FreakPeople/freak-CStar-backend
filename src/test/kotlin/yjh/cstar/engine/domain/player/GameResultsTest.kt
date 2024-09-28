package yjh.cstar.engine.domain.player

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import yjh.cstar.common.BaseException
import kotlin.test.assertEquals

@DisplayName("[Domain 테스트] Players")
class GameResultsTest {

    @Test
    fun `플레이어 목록 생성 테스트`() {
        // given
        val players: Map<Long, String> = mapOf(1L to "player1", 2L to "player2")

        // when & then
        assertDoesNotThrow { Players(players) }
    }

    @Test
    fun `플레이어 목록 생성시 비어있는 파라미터가 들어오면 예외발생`() {
        // given
        val players: Map<Long, String> = mapOf()

        // when & then
        assertThrows<BaseException> { Players(players) }
    }

    @Test
    fun `특정 id인 회원의 닉네임 조회 테스트`() {
        // given
        val players = Players(mapOf(1L to "player1", 2L to "player2"))

        // when
        val nickname = players.getNickname(1L)

        // then
        assertEquals("player1", nickname)
    }

    @Test
    fun `모든 플레이어의 id 조회 테스트`() {
        // given
        val players = Players(mapOf(1L to "player1", 2L to "player2"))

        // when
        val playerIds = players.getPlayerIds()

        // then
        assertEquals(2, playerIds.size)
    }
}
