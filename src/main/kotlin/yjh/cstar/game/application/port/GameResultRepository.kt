package yjh.cstar.game.application.port

import yjh.cstar.game.domain.GameResult

interface GameResultRepository {
    fun save(gameResult: GameResult): GameResult
}
