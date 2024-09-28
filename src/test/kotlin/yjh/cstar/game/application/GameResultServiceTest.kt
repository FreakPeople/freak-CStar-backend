package yjh.cstar.game.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import yjh.cstar.IntegrationTest
import yjh.cstar.game.domain.GameResultCreateCommand
import yjh.cstar.game.infrastructure.jpa.GameJpaRepository
import yjh.cstar.game.infrastructure.jpa.GameResultJpaRepository
import yjh.cstar.room.domain.RoomStatus
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository
import java.time.LocalDateTime
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

        val ranking = linkedMapOf(
            "player:2" to 5,
            "player:3" to 3,
            "player:1" to 2
        )
        val gameResultCreateCommand = GameResultCreateCommand(
            ranking,
            roomId,
            2L,
            10,
            1L,
            LocalDateTime.now()
        )
        // when
        gameResultService.create(gameResultCreateCommand)

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
