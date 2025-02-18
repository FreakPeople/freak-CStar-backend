package yjh.cstar.workbook.domain

import java.time.LocalDateTime

class WorkBook(
    val id: Long = 0,
    val writerId: Long,
    val title: String,
    val description: String,
    val workbookType: WorkBookType,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {

    companion object {
        fun create(command: WorkBookCreateCommand): WorkBook {
            return WorkBook(
                writerId = command.writerId,
                title = command.title,
                description = command.description,
                workbookType = command.workbookType,
                createdAt = command.createdAt
            )
        }
    }
}
