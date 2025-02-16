package yjh.cstar.game.infrastructure

import org.springframework.stereotype.Repository
import yjh.cstar.game.application.port.GameResultRepository
import yjh.cstar.game.domain.GameResult
import yjh.cstar.game.infrastructure.jpa.GameResultEntity
import yjh.cstar.game.infrastructure.jpa.GameResultJpaRepository

@Repository
class GameResultRepositoryAdapter(
    private val gameResultJpaRepository: GameResultJpaRepository,
) : GameResultRepository {

    override fun save(gameResult: GameResult): GameResult {
        return gameResultJpaRepository.save(GameResultEntity.from(gameResult)).toModel()
    }

    override fun saveAll(gameResults: List<GameResult>) {
        gameResultJpaRepository.saveAll(gameResults.map { GameResultEntity.from(it) })
    }
}
