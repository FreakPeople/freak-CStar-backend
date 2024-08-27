package yjh.cstar.quiz.infrastructure

import org.springframework.stereotype.Repository
import yjh.cstar.quiz.application.port.QuizRepository
import yjh.cstar.quiz.domain.Quiz
import yjh.cstar.quiz.infrastructure.jpa.QuizEntity
import yjh.cstar.quiz.infrastructure.jpa.QuizJpaRepository

@Repository
class QuizRepositoryAdapter(
    private val quizJpaRepository: QuizJpaRepository,
) : QuizRepository {
    override fun save(quiz: Quiz): Quiz {
        return quizJpaRepository.save(QuizEntity.from(quiz)).toModel()
    }

    override fun getQuizzes(quizCategory: String, totalQuestions: Int): List<Quiz> {
        return quizJpaRepository.getQuizzes(quizCategory, totalQuestions)
            .map { quizEntity -> quizEntity.toModel() }
    }
}
