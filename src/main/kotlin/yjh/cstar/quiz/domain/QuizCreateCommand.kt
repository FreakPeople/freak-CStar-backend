package yjh.cstar.quiz.domain

class QuizCreateCommand(
    val question: String,
    val answer: String,
    val category: Category,
)
