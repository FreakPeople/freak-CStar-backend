package yjh.cstar.engine.domain.quiz

data class QuizDto(val id: Long, val question: String, val answer: String)

class Quiz(val id: Long, val question: String, private val answer: String) {

    companion object {
        fun of(id: Long, question: String, answer: String) = Quiz(id, question, answer)
    }

    fun isCorrectAnswer(playerAnswer: PlayerAnswer) = playerAnswer.isMatch(answer)
}
