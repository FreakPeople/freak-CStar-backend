package yjh.cstar.game.domain

import yjh.cstar.engine.infrastructure.RedisRankingHandler

class GameResults(
    val records: List<GameResult>,
) {

    companion object {
        private const val INITIAL_RANK = 1

        fun create(command: GameResultCreateCommand, game: Game): GameResults {
            val records = mutableListOf<GameResult>()

            var rank = INITIAL_RANK
            for ((value, score) in command.ranking) {
                val playerId = RedisRankingHandler.getPlayerIdOf(value)
                val record = createRecord(game, command, playerId, score, rank++)
                records.add(record)
            }

            return GameResults(records)
        }

        private fun createRecord(
            game: Game,
            command: GameResultCreateCommand,
            playerId: Long,
            score: Int,
            rank: Int,
        ) = GameResult(
            gameId = game.id,
            playerId = playerId,
            totalCount = command.totalQuizSize,
            correctCount = score,
            ranking = rank
        )
    }
}
