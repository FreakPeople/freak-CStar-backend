package yjh.cstar.engine.domain.quiz

class Quiz(val id: Long, val question: String, private val answer: String) {

    companion object {
        fun of(id: Long, question: String, answer: String) = Quiz(id, question, answer)
    }

    fun isSameAnswer(answer: String) = this.answer == answer
}
