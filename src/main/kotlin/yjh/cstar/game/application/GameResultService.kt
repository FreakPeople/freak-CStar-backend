package yjh.cstar.game.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.game.application.port.GameRepository
import yjh.cstar.game.application.port.GameResultRepository
import yjh.cstar.game.domain.Game
import yjh.cstar.game.domain.GameResult
import yjh.cstar.game.presentation.request.RankingCreateRequest
import yjh.cstar.room.application.RoomService

@Service
class GameResultService(
    private val gameRepository: GameRepository,
    private val gameResultRepository: GameResultRepository,
    private val roomService: RoomService,
) {
    @Transactional
    fun create(request: RankingCreateRequest) {
        val game = Game(
            roomId = request.roomId,
            winnerId = request.winningPlayerId,
            totalQuizCount = request.totalQuizSize,
            categoryId = request.categoryId,
            startedAt = request.gameStartedAt
        )
        val savedGame = gameRepository.save(game)

        for ((idx, result) in request.sortedRanking.entries.withIndex()) {
            val rank = idx + 1
            val playerId = result.key
            val score = result.value

            val gameResult = GameResult(
                gameId = savedGame.id,
                playerId = playerId,
                totalCount = request.totalQuizSize,
                correctCount = score,
                ranking = rank
            )
            gameResultRepository.save(gameResult)
        }

        roomService.endGameAndResetRoom(savedGame.roomId)
    }
}
