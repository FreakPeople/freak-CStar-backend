package yjh.cstar.play.domain.ranking

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

@DisplayName("[Domain 테스트] Ranking")
class RankingTest {

    @Test
    fun `랭킹 생성 테스트`() {
        // given
        val rankingRawData = listOf(
            Pair("player1", 5.0),
            Pair("player2", 10.0),
            Pair("player3", 2.0)
        )

        // when & then
        assertDoesNotThrow { Ranking.of(rankingRawData) }
    }

    @Test
    fun `정렬된 랭킹 조회 테스트`() {
        // given
        val rankingRawData = listOf(
            Pair("player1", 5.0),
            Pair("player2", 10.0),
            Pair("player3", 2.0)
        )
        val ranking = Ranking.of(rankingRawData)

        // when
        val sortedRanking = ranking.getRanking()
        val (key, score) = sortedRanking.entries.first()

        // then
        assertEquals(10, score)
        assertEquals("player2", key)
    }
}
