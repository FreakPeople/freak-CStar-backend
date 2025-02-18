package yjh.cstar.category.domain

import java.time.LocalDateTime

class Category(
    val id: Long = 0,
    val category: CategoryType,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
)
