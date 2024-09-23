package yjh.cstar.engine.domain.quiz

class PlayerAnswer(
    val roomId: Long,
    val playerId: Long,
    val quizId: Long,
    val playerAnswer: String
) {
    
    fun isCorrect(quiz: Quiz) = quiz.isSameAnswer(playerAnswer)
}
