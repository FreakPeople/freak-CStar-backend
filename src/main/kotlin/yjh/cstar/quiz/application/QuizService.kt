package yjh.cstar.quiz.application

import org.springframework.stereotype.Service
import yjh.cstar.quiz.application.port.QuizRepository
import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.domain.Quiz

@Service
class QuizService(
    val quizRepository: QuizRepository,
) {
    fun getQuizzes(quizCategory: String, totalQuestions: Int): List<Quiz> {
        val category = Category.create(quizCategory)
        return quizRepository.getQuizzes(category.name, totalQuestions)
    }
}
