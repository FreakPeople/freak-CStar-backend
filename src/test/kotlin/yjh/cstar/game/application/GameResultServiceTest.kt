package yjh.cstar.game.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import yjh.cstar.IntegrationTest
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.game.infrastructure.jpa.GameEntity
import yjh.cstar.game.infrastructure.jpa.GameJpaRepository
import yjh.cstar.game.infrastructure.jpa.GameResultJpaRepository
import yjh.cstar.room.domain.RoomStatus
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository
import java.time.LocalDateTime
import java.util.TreeMap
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
            totalQuizSize = 10,
            categoryId = 1L
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

    @Test
    fun `게임 종료 후 게임 방 상태 및 현재 방 인원 수 초기화 테스트`() {
        // given & when
        val savedRoom = roomJpaRepository.save(
            RoomEntity(
                maxCapacity = 5,
                currCapacity = 3,
                status = RoomStatus.IN_PROGRESS,
                createdAt = null,
                updatedAt = null
            )
        ).toModel()

        gameJpaRepository.save(
            GameEntity(
                roomId = savedRoom.id,
                winnerId = 1L,
                totalQuizCount = 5,
                categoryId = 1L,
                startedAt = LocalDateTime.now(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        ).toModel()

        val ranking = TreeMap<Long, Int>().apply {
            put(1L, 3)
            put(2L, 2)
        }

        gameResultService.create(
            ranking = ranking,
            gameStartedAt = LocalDateTime.now(),
            roomId = savedRoom.id,
            winningPlayerId = 1L,
            totalQuizSize = 5,
            categoryId = 1L
        )

        // then
        val updatedRoom = roomJpaRepository.findByIdOrNull(savedRoom.id)
            ?: throw BaseException(BaseErrorCode.NOT_FOUND_ROOM)

        val updatedRoomCurrCapacity = updatedRoom.toModel().currCapacity
        val updatedRoomStatus = updatedRoom.toModel().status

        with(updatedRoom) {
            assertNotNull(this)
            assertEquals(0, updatedRoomCurrCapacity)
            assertEquals(RoomStatus.WAITING, updatedRoomStatus)
        }
    }
}
