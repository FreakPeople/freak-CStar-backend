package yjh.cstar.quiz.application.port

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import yjh.cstar.quiz.domain.Quiz

interface QuizRepository {
    fun save(quiz: Quiz): Quiz
    fun getQuizzes(quizCategoryId: Long, totalQuestions: Int): List<Quiz>
    fun findAllByCategory(quizCategoryId: Long, pageable: Pageable): Page<Quiz>
    fun findAllCreatedByMember(writerId: Long, pageable: Pageable): Page<Quiz>
    fun findAllAttemptedByMember(memberId: Long, pageable: Pageable): Page<Quiz>
}
