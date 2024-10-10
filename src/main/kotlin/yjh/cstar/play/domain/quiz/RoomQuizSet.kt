package yjh.cstar.play.domain.quiz

class RoomQuizSet(private val quizzes: List<Quiz>) {

    companion object {
        fun of(quizzes: List<Quiz>) = RoomQuizSet(quizzes)
    }

    fun getQuizList() = quizzes

    fun getSize() = quizzes.size
}
