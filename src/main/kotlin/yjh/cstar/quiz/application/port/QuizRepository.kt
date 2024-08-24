package yjh.cstar.quiz.application.port

import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.domain.Quiz

interface QuizRepository {
    fun getQuizzes(quizCategory: Category, totalQuestions: Int): List<Quiz>
}
