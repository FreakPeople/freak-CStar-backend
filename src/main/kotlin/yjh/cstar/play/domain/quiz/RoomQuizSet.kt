package yjh.cstar.play.domain.quiz

class RoomQuizSet(
    private val quizzes: List<Quiz>,
) {
    private var currQuizNo: Int = 0

    companion object {
        fun of(quizzes: List<Quiz>): RoomQuizSet {
            require(quizzes.isNotEmpty()) { "[ERROR] 퀴즈 문제 리스트가 존재하지 않습니다." }

            return RoomQuizSet(quizzes)
        }
    }

    fun isRunning(): Boolean {
        return currQuizNo < quizzes.size
    }

    fun getNextQuizInfo(): QuizInfo {
        validateFinished()

        val currQuiz = quizzes[currQuizNo++]
        return QuizInfo(currQuizNo, currQuiz.id, currQuiz)
    }

    fun getQuizList(): List<Quiz> =
        quizzes

    fun getSize(): Int =
        quizzes.size

    private fun validateFinished() {
        if (currQuizNo >= quizzes.size) {
            throw IllegalArgumentException("[ERROR] 퀴즈 문제가 끝났습니다. 남아있는 퀴즈가 없습니다.")
        }
    }
}
