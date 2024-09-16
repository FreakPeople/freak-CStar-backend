package yjh.cstar.engine.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import yjh.cstar.IntegrationTest
import yjh.cstar.common.BaseException
import yjh.cstar.engine.domain.Ranking
import java.util.TreeMap
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("[Application 테스트] RankingService")
class RankingServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var rankingService: RankingService

    @Test
    fun `퀴즈별 플레이어 랭킹 계산 테스트`() {
        // given
        val players = listOf(1L, 2L, 3L)
        val playerScores = TreeMap<Long, Int>().apply {
            put(1L, 2)
            put(2L, 3)
            put(3L, 1)
        }
        val nicknames = mutableMapOf<Long, String>().apply {
            put(1L, "플레이어1")
            put(2L, "플레이어2")
            put(3L, "플레이어3")
        }
        val ranking = Ranking(players, playerScores)

        // when
        val rankingMessage = rankingService.getRankingMessage(ranking, nicknames)

        // then
        val expected = "[1등 플레이어2-3]  [2등 플레이어1-2]  [3등 플레이어3-1]  "
        assertEquals(expected, rankingMessage)
    }

    @Test
    fun `최종 게임 결과 계산 테스트 - 최종 1등인 플레이어 아이디를 반환한다`() {
        // given
        val players = listOf(1L, 2L, 3L)
        val playerScores = TreeMap<Long, Int>().apply {
            put(1L, 2)
            put(2L, 3)
            put(3L, 1)
        }
        val ranking = Ranking(players, playerScores)

        // when
        val winningPlayerId = ranking.getWinnerId()

        // then
        assertNotNull(winningPlayerId)
        assertEquals(winningPlayerId, 2L)
    }

    @Test
    fun `최종 게임 결과 계산 테스트 - 아무도 퀴즈를 풀지 못했을 경우 -1을 반환한다`() {
        // given
        val players = listOf(1L, 2L, 3L)
        val ranking = Ranking(players)

        // when
        val winningPlayerId = ranking.getWinnerId()

        // then
        assertEquals(-1, winningPlayerId)
    }

    @Test
    fun `최종 게임 결과 계산 테스트 - 플레이어가 존재하지 않을 경우 예외를 반환한다`() {
        // given
        val players = emptyList<Long>()
        val ranking = Ranking(players)

        // when & then
        assertThrows<BaseException> { ranking.getWinnerId() }
    }
}
