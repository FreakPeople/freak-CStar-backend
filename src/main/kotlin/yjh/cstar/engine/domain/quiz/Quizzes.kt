package yjh.cstar.engine.domain.quiz

class Quizzes(private val quizzes: List<Quiz>) {

    companion object {
        fun of(quizzes: List<QuizDto>): Quizzes {
            val quizList = quizzes.map { quizDto -> quizDto.toModel() }
            return Quizzes(quizList)
        }
    }

    fun getQuizList() = quizzes

    fun getSize() = quizzes.size
}
