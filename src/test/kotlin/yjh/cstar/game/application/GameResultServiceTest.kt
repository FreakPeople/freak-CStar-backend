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
import yjh.cstar.game.presentation.request.RankingCreateRequest
import yjh.cstar.room.domain.RoomStatus
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository
import java.time.LocalDateTime
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

        val ranking = listOf(
            "player:2" to 5.0,
            "player:3" to 3.0,
            "player:1" to 2.0
        )
        val rankingCreateRequest = RankingCreateRequest(
            ranking,
            roomId,
            2L,
            10,
            1L,
            LocalDateTime.now()
        )
        // when
        gameResultService.create(rankingCreateRequest)

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

        val ranking = listOf(
            "player:2" to 5.0,
            "player:3" to 3.0,
            "player:1" to 2.0
        )
        val rankingCreateRequest = RankingCreateRequest(
            ranking,
            savedRoom.id,
            1L,
            5,
            1L,
            LocalDateTime.now()
        )
        gameResultService.create(rankingCreateRequest)

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
