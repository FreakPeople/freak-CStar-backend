package yjh.cstar.play.domain.game

import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.quiz.Quiz
import yjh.cstar.play.domain.quiz.RoomQuizSet

class GameInfo(
    val players: Players,
    val roomQuizSet: RoomQuizSet,
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
                RoomQuizSet.of(quizzes),
                roomId,
                categoryId
            )
        }
    }
}
