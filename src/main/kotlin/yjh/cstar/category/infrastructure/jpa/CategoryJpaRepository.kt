package yjh.cstar.category.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryJpaRepository : JpaRepository<CategoryEntity, Long>
