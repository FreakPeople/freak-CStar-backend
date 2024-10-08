package yjh.cstar.quiz.infrastructure

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

    override fun getRandomQuizzes(quizCategoryId: Long, totalQuestions: Int): List<Quiz> {
        return quizJpaRepository.getRandomQuizzes(quizCategoryId, totalQuestions)
            .map { quizEntity -> quizEntity.toModel() }
    }

    override fun findAllByCategory(quizCategoryId: Long, pageable: Pageable): Page<Quiz> {
        return quizJpaRepository.findAllByCategory(quizCategoryId, pageable).map { it.toModel() }
    }

    override fun findAllCreatedByMember(writerId: Long, pageable: Pageable): Page<Quiz> {
        return quizJpaRepository.findAllCreatedByMember(writerId, pageable).map { it.toModel() }
    }

    override fun findAllAttemptedByMember(memberId: Long, pageable: Pageable): Page<Quiz> {
        return quizJpaRepository.findAllAttemptedByMember(memberId, pageable).map { it.toModel() }
    }
}
