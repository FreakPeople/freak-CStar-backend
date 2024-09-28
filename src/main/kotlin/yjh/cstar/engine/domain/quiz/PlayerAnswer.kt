package yjh.cstar.engine.domain.quiz

class PlayerAnswer(
    val answer: String,
    val quizId: Long,
    val roomId: Long,
    val playerId: Long,
    val nickname: String,
) {

    fun isCorrect(quiz: Quiz) = quiz.isSameAnswer(answer)
}
