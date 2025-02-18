package yjh.cstar.category.infrastructure.jpa

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
import yjh.cstar.category.domain.Category
import yjh.cstar.category.domain.CategoryType
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "quiz_category")
@Entity
class CategoryEntity(
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "category_name", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: CategoryType,

    @CreatedDate
    @Column(name = "created_at")
    private var createdAt: LocalDateTime?,

    @LastModifiedDate
    @Column(name = "updated_at")
    private var updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(category: Category): CategoryEntity {
            return CategoryEntity(
                id = category.id,
                category = category.category,
                createdAt = category.createdAt,
                updatedAt = category.updatedAt,
                deletedAt = category.deletedAt
            )
        }
    }

    fun toModel(): Category {
        return Category(
            id = this.id,
            category = this.category,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }
}
