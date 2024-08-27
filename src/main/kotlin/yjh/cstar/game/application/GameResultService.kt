package yjh.cstar.game.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.game.application.port.GameRepository
import yjh.cstar.game.application.port.GameResultRepository
import yjh.cstar.game.domain.Game
import yjh.cstar.game.domain.GameResult
import yjh.cstar.room.application.RoomService
import java.time.LocalDateTime
import java.util.*

@Service
class GameResultService(
    private val gameRepository: GameRepository,
    private val gameResultRepository: GameResultRepository,
    private val roomService: RoomService,
) {
    @Transactional
    fun create(
        ranking: TreeMap<Long, Int>,
        gameStartedAt: LocalDateTime,
        roomId: Long,
        winningPlayerId: Long,
        totalQuizSize: Int,
    ) {
        // 게임 전체 결과
        val game = Game(
            roomId = roomId,
            winnerId = winningPlayerId,
            totalQuizCount = totalQuizSize,
            startedAt = gameStartedAt
        )
        val savedGame = gameRepository.save(game)

        // 한 사람당 게임 결과
        val players: List<Long> = roomService.retrieveCurrParticipant(roomId)

        players.forEach {
            val currentValue = ranking.getOrDefault(it, 0)
            ranking[it] = currentValue
        }

        val sortedEntries = ranking.entries.sortedByDescending { it.value }

        for ((idx, result) in sortedEntries.withIndex()) {
            val rank = idx + 1
            val playerId = result.key
            val score = result.value

            val gameResult = GameResult(
                gameId = savedGame.id,
                playerId = playerId,
                totalCount = totalQuizSize,
                correctCount = score,
                ranking = rank
            )
            gameResultRepository.save(gameResult)
        }
    }
}
