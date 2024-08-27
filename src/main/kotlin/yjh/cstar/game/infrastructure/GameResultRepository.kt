package yjh.cstar.game.infrastructure

import yjh.cstar.game.application.port.GameResultRepository
import yjh.cstar.game.domain.GameResult
import yjh.cstar.game.infrastructure.jpa.GameResultEntity
import yjh.cstar.game.infrastructure.jpa.GameResultJpaRepository

class GameResultRepository(
    private val gameResultJpaRepository: GameResultJpaRepository,
) : GameResultRepository {
    override fun save(gameResult: GameResult): GameResult {
        return gameResultJpaRepository.save(GameResultEntity.from(gameResult)).toModel()
    }
}
