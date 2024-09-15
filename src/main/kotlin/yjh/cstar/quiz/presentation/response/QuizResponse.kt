package yjh.cstar.quiz.presentation.response

import yjh.cstar.quiz.domain.Quiz

data class QuizResponse(
    val writerId: Long,
    val question: String,
    val answer: String,
    val categoryId: Long,
) {
    companion object {
        fun from(quiz: Quiz): QuizResponse {
            return QuizResponse(
                writerId = quiz.writerId,
                question = quiz.question,
                answer = quiz.answer,
                categoryId = quiz.categoryId
            )
        }
    }
}
