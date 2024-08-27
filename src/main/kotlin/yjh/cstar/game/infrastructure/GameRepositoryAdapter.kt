package yjh.cstar.game.infrastructure

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.cstar.game.application.port.GameRepository
import yjh.cstar.game.domain.Game
import yjh.cstar.game.infrastructure.jpa.GameEntity
import yjh.cstar.game.infrastructure.jpa.GameJpaRepository

@Repository
class GameRepositoryAdapter(
    private val gameJpaRepository: GameJpaRepository,
) : GameRepository {
    override fun findByIdOrNull(id: Long): Game? {
        return gameJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun save(game: Game): Game {
        return gameJpaRepository.save(GameEntity.from(game)).toModel()
    }
}
