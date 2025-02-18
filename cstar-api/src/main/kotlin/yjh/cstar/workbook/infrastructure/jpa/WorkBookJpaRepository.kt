package yjh.cstar.workbook.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface WorkBookJpaRepository : JpaRepository<WorkBookEntity, Long>
