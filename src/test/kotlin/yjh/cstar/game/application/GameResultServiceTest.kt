package yjh.cstar.game.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import yjh.cstar.IntegrationTest
import yjh.cstar.game.infrastructure.jpa.GameJpaRepository
import yjh.cstar.game.infrastructure.jpa.GameResultJpaRepository
import yjh.cstar.room.domain.RoomStatus
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

@DisplayName("[Application 테스트] GameResultService")
class GameResultServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var roomJpaRepository: RoomJpaRepository

    @Autowired
    private lateinit var gameResultService: GameResultService

    @Autowired
    private lateinit var gameJpaRepository: GameJpaRepository

    @Autowired
    private lateinit var gameResultJpaRepository: GameResultJpaRepository

    @Test
    fun `게임 결과 생성 테스트`() {
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

        val ranking = TreeMap<Long, Int>()
        ranking[2L] = 5
        ranking[3L] = 3
        ranking[1L] = 2

        // when
        gameResultService.create(
            ranking = ranking,
            gameStartedAt = LocalDateTime.now(),
            roomId = roomId,
            winningPlayerId = 2L,
            totalQuizSize = 10
        )

        // then
        // 1. Game
        val games = gameJpaRepository.findAll().map { it.toModel() }
        assertEquals(1, games.size)
        assertEquals(roomId, games[0].roomId)
        assertEquals(2L, games[0].winnerId)
        assertEquals(10, games[0].totalQuizCount)

        // 2. GameResult
        val gameResults = gameResultJpaRepository.findAll().map { it.toModel() }
        assertEquals(3, gameResults.size)
    }
}
