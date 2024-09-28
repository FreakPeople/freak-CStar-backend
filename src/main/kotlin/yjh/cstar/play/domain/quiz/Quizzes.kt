package yjh.cstar.play.domain.quiz

class Quizzes(private val quizzes: List<Quiz>) {

    companion object {
        fun of(quizzes: List<Quiz>) = Quizzes(quizzes)
    }

    fun getQuizList() = quizzes

    fun getSize() = quizzes.size
}
