package yjh.cstar.quiz.presentation.request

import yjh.cstar.quiz.domain.QuizCreateCommand

data class QuizCreateRequest(
    val question: String,
    val answer: String,
    val categoryId: Long,
)

fun QuizCreateRequest.toCommand() = QuizCreateCommand(
    question = question,
    answer = answer,
    categoryId = categoryId
)
