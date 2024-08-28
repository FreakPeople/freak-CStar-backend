package yjh.cstar.quiz.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.quiz.application.port.QuizRepository
import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.domain.Quiz
import yjh.cstar.quiz.domain.QuizCreateCommand

@Transactional(readOnly = true)
@Service
class QuizService(
    val quizRepository: QuizRepository,
) {
    fun getQuizzes(quizCategory: String, totalQuestions: Int): List<Quiz> {
        val category = Category.create(quizCategory)
        return quizRepository.getQuizzes(category.name, totalQuestions)
    }

    @Transactional
    fun create(command: QuizCreateCommand, writerId: Long): Long {
        val quiz = Quiz.create(command, writerId)
        return quizRepository.save(quiz).id
    }
}
