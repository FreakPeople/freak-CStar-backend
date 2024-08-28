package yjh.cstar.quiz.application.port

import yjh.cstar.quiz.domain.Quiz

interface QuizRepository {
    fun save(quiz: Quiz): Quiz
    fun getQuizzes(quizCategory: String, totalQuestions: Int): List<Quiz>
}
