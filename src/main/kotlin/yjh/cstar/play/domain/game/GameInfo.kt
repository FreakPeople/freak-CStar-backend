package yjh.cstar.play.domain.game

import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.quiz.Quiz
import yjh.cstar.play.domain.quiz.Quizzes

class GameInfo(
    val players: Players,
    val quizzes: Quizzes,
    val roomId: Long,
    val categoryId: Long,
) {

    companion object {
        fun of(
            players: Map<Long, String>,
            quizzes: List<Quiz>,
            roomId: Long,
            categoryId: Long,
        ): GameInfo {
            return GameInfo(
                Players.of(players),
                Quizzes.of(quizzes),
                roomId,
                categoryId
            )
        }
    }
}
