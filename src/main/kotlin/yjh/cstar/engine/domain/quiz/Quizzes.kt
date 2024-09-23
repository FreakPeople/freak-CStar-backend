package yjh.cstar.engine.domain.quiz

class Quizzes(private val quizzes: List<Quiz>) {
    companion object {
        fun of(quizzes: List<QuizDto>): Quizzes {
            return Quizzes(quizzes.map { quizDto -> quizDto.toModel() })
        }
    }

    fun getQuizList(): List<Quiz> {
        return quizzes
    }

    fun getSize(): Int {
        return quizzes.size
    }
}
