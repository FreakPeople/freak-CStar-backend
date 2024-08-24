package yjh.cstar.quiz.domain

import java.time.LocalDateTime

class Quiz(
    val id: Long = 0,
    val writerId: Long,
    val question: String,
    val answer: String,
    val category: Category,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
)
