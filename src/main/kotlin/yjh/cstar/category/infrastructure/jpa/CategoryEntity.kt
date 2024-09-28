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
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.cstar.category.domain.Category
import yjh.cstar.category.domain.CategoryType

@EntityListeners(AuditingEntityListener::class)
@Table(name = "category")
@Entity
class CategoryEntity(
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: CategoryType,
) {
    companion object {
        fun from(category: Category): CategoryEntity {
            return CategoryEntity(
                id = category.id,
                category = category.category
            )
        }
    }

    fun toModel(): Category {
        return Category(
            id = this.id,
            category = this.category
        )
    }
}
