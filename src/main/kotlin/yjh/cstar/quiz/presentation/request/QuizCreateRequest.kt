package yjh.cstar.quiz.presentation.request

import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.domain.QuizCreateCommand

data class QuizCreateRequest(
    val question: String,
    val answer: String,
    val category: String,
)

fun QuizCreateRequest.toCommand() = QuizCreateCommand(
    question = question,
    answer = answer,
    category = Category.create(category)
)