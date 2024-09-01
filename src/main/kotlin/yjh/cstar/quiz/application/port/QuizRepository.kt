package yjh.cstar.quiz.application.port

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.domain.Quiz

interface QuizRepository {
    fun save(quiz: Quiz): Quiz
    fun getQuizzes(quizCategory: String, totalQuestions: Int): List<Quiz>
    fun findAllByCategory(category: Category, pageable: Pageable): Page<Quiz>
}
