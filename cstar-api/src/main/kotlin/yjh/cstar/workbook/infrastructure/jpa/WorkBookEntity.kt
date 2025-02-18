package yjh.cstar.workbook.infrastructure.jpa

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
import yjh.cstar.workbook.domain.WorkBook
import yjh.cstar.workbook.domain.WorkBookType
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "workbook")
@Entity
class WorkBookEntity(
    @Id
    @Column(name = "workbook_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "writer_id", nullable = false)
    private val writerId: Long,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val workbookType: WorkBookType,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private var createdAt: LocalDateTime?,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private var updatedAt: LocalDateTime?,
) {
    companion object {
        fun from(workBook: WorkBook): WorkBookEntity {
            return WorkBookEntity(
                id = workBook.id,
                writerId = workBook.writerId,
                title = workBook.title,
                description = workBook.description,
                workbookType = workBook.workbookType,
                createdAt = workBook.createdAt,
                updatedAt = workBook.updatedAt
            )
        }
    }
    fun toModel(): WorkBook {
        return WorkBook(
            id = id,
            writerId = writerId,
            title = title,
            description = description,
            workbookType = workbookType,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
