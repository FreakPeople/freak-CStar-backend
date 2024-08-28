package yjh.cstar.quiz.domain

import java.time.LocalDateTime

class Quiz(
    val id: Long = 0,
    val writerId: Long,
    val question: String,
    val answer: String,
    val category: Category,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun create(command: QuizCreateCommand, writerId: Long): Quiz {
            return Quiz(
                writerId = writerId,
                question = command.question,
                answer = command.answer,
                category = command.category
            )
        }
    }
}
