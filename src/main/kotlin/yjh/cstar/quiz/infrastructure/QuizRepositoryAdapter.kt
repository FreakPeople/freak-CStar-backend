package yjh.cstar.quiz.infrastructure

import org.springframework.stereotype.Repository
import yjh.cstar.quiz.application.port.QuizRepository
import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.domain.Quiz
import yjh.cstar.quiz.infrastructure.jpa.QuizJpaRepository

@Repository
class QuizRepositoryAdapter(
    private val quizJpaRepository: QuizJpaRepository,
) : QuizRepository {

    override fun getQuizzes(quizCategory: Category, totalQuestions: Int): List<Quiz> {
        return quizJpaRepository.getQuizzes(quizCategory, totalQuestions)
            .map { quizEntity -> quizEntity.toModel() }
    }
}
