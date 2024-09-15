package yjh.cstar.engine.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import yjh.cstar.IntegrationTest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("[Application 테스트] RankingService")
class RankingServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var rankingService: RankingService

    @Test
    fun `퀴즈별 플레이어 랭킹 계산 테스트`() {
        // given
        val ranking = TreeMap<Long, Int>()
        val nicknames = mutableMapOf<Long, String>()

        ranking[1L] = 2
        ranking[2L] = 3
        ranking[3L] = 1

        nicknames[1L] = "플레이어1"
        nicknames[2L] = "플레이어2"
        nicknames[3L] = "플레이어3"

        // when
        val rankingMessage = rankingService.generateRanking(ranking, nicknames)

        // then
        val expected = "[1등 플레이어2-3]  [2등 플레이어1-2]  [3등 플레이어3-1]  "
        assertEquals(expected, rankingMessage)
    }

    @Test
    fun `최종 게임 결과 계산 테스트 - 최종 1등인 플레이어 아이디를 반환한다`() {
        // given
        val ranking = TreeMap<Long, Int>()

        ranking[1L] = 2
        ranking[2L] = 3
        ranking[3L] = 1

        // when
        val winningPlayerId = rankingService.calculateGameResult(ranking)

        // then
        assertNotNull(winningPlayerId)
        assertEquals(winningPlayerId, 2L)
    }

    @Test
    fun `최종 게임 결과 계산 테스트 - 아무도 퀴즈를 풀지 못했을 경우 -1을 반환한다 `() {
        // given
        val ranking = TreeMap<Long, Int>()

        // when
        val winningPlayerId = rankingService.calculateGameResult(ranking)

        // then
        assertEquals(winningPlayerId, -1)
    }
}
