package yjh.cstar.play.domain.game

import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.quiz.Quiz
import yjh.cstar.play.domain.quiz.QuizDto
import yjh.cstar.play.domain.quiz.Quizzes

data class GameInfo(
    val players: Players,
    val quizzes: Quizzes,
    val roomId: Long,
    val categoryId: Long,
) {

    companion object {
        fun of(players: Map<Long, String>, quizzes: List<QuizDto>, roomId: Long, categoryId: Long): GameInfo {
            val quizList = quizzes.map { quizDto -> Quiz.of(quizDto.id, quizDto.question, quizDto.answer) }
            return GameInfo(Players.of(players), Quizzes.of(quizzes), roomId, categoryId)
        }
    }
}
