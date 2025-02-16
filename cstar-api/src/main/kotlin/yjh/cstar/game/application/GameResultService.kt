package yjh.cstar.game.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.game.application.port.GameRepository
import yjh.cstar.game.application.port.GameResultRepository
import yjh.cstar.game.domain.Game
import yjh.cstar.game.domain.GameResultCreateCommand
import yjh.cstar.game.domain.GameResults
import yjh.cstar.room.application.RoomService

@Service
class GameResultService(
    private val gameRepository: GameRepository,
    private val gameResultRepository: GameResultRepository,
    private val roomService: RoomService,
) {
    @Transactional
    fun create(command: GameResultCreateCommand) {
        val game = Game.create(command)
            .let { gameRepository.save(it) }

        val gameResults = GameResults.create(command, game)

        gameResultRepository.saveAll(gameResults.records)
    }
}
