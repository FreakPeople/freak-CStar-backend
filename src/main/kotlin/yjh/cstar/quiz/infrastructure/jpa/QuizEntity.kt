package yjh.cstar.quiz.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.domain.Quiz
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "quiz")
@Entity
class QuizEntity(
    @Id
    @Column(name = "quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "member_id", nullable = false)
    private val writerId: Long,

    @Column(name = "question", nullable = false)
    val question: String,

    @Column(name = "answer", nullable = false)
    val answer: String,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: Category,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private var createdAt: LocalDateTime?,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private var updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(quiz: Quiz): QuizEntity {
            return QuizEntity(
                id = quiz.id,
                writerId = quiz.writerId,
                question = quiz.question,
                answer = quiz.answer,
                category = quiz.category,
                createdAt = quiz.createdAt,
                updatedAt = quiz.updatedAt,
                deletedAt = quiz.deletedAt
            )
        }
    }

    fun toModel(): Quiz {
        return Quiz(
            id = this.id,
            writerId = this.writerId,
            question = this.question,
            answer = this.answer,
            category = this.category,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }
}
