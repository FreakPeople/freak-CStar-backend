package yjh.cstar.engine.domain

import yjh.cstar.quiz.domain.Quiz

class Quizzes(
    private val quizzes: List<Quiz>,
) {
    fun getSize() = quizzes.size

    fun getQuizList() = quizzes
}
