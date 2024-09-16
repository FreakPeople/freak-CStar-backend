package yjh.cstar.engine.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import yjh.cstar.IntegrationTest
import java.util.*
import kotlin.test.assertEquals

@DisplayName("[Application 테스트] ScoreManagementService")
class ScoreManagementServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var scoreManagementService: ScoreManagementService

    @Test
    fun `점수 갱신 테스트 - 정답을 맞춘 플레이어의 점수를 1점 올린다`() {
        // given
        val ranking = TreeMap<Long, Int>()
        ranking[1L] = 3
        val playerId = 1L

        // when
        scoreManagementService.updateScore(ranking, playerId)

        // then
        assertEquals(4, ranking[playerId])
    }
}
