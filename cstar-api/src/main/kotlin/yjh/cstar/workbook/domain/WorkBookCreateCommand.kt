package yjh.cstar.workbook.domain

import java.time.LocalDateTime

data class WorkBookCreateCommand(
    val writerId: Long,
    val title: String,
    val description: String,
    val workbookType: WorkBookType,
    val createdAt: LocalDateTime,
)
