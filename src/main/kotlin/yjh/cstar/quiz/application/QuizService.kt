package yjh.cstar.quiz.application

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.category.infrastructure.jpa.CategoryJpaRepository
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.quiz.application.port.QuizFilter.QuizFilterFactory
import yjh.cstar.quiz.application.port.QuizRepository
import yjh.cstar.quiz.domain.Quiz
import yjh.cstar.quiz.domain.QuizCreateCommand

@Transactional(readOnly = true)
@Service
class QuizService(
    val quizRepository: QuizRepository,
    val categoryJpaRepository: CategoryJpaRepository,
    val quizFilterFactory: QuizFilterFactory,
) {
    fun getQuizzes(quizCategoryId: Long, totalQuestions: Int): List<Quiz> {
        checkQuizCategoryIdValidate(quizCategoryId)
        return quizRepository.getQuizzes(quizCategoryId, totalQuestions)
    }

    @Transactional
    fun create(command: QuizCreateCommand, writerId: Long): Long {
        val quiz = Quiz.create(command, writerId)
        return quizRepository.save(quiz).id
    }

    fun retrieveAllByCategory(quizCategoryId: Long, pageable: Pageable): Page<Quiz> {
        checkQuizCategoryIdValidate(quizCategoryId)
        return quizRepository.findAllByCategory(quizCategoryId, pageable)
    }

    fun retrieveAllByQuizFilterType(memberId: Long, quizFilter: String, pageable: Pageable): Page<Quiz> {
        val quizFilterType = quizFilterFactory.filterType(quizFilter)
        return quizFilterType.filter(memberId, pageable)
    }

    private fun checkQuizCategoryIdValidate(quizCategoryId: Long) {
        categoryJpaRepository.findByIdOrNull(quizCategoryId)
            ?: throw BaseException(BaseErrorCode.QUIZ_CATEGORY_INVALID)
    }
}
