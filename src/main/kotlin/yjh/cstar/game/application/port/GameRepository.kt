package yjh.cstar.game.application.port

import yjh.cstar.game.domain.Game

interface GameRepository {
    fun findByIdOrNull(id: Long): Game?

    fun save(game: Game): Game
}
