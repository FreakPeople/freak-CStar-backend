package yjh.cstar.engine.domain.game

import yjh.cstar.engine.domain.player.Players
import yjh.cstar.engine.domain.quiz.Quiz
import yjh.cstar.engine.domain.quiz.QuizDto

data class GameInfo(
    val players: Players,
    val quizzes: List<Quiz>,
    val roomId: Long,
    val categoryId: Long,
) {

    companion object {
        fun of(players: Map<Long, String>, quizzes: List<QuizDto>, roomId: Long, categoryId: Long): GameInfo {
            val quizList = quizzes.map { quizDto -> Quiz.of(quizDto.id, quizDto.question, quizDto.answer) }
            return GameInfo(Players.of(players), quizList, roomId, categoryId)
        }
    }
}
