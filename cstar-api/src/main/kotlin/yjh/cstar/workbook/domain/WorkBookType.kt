package yjh.cstar.workbook.domain

import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.exception.BaseException

enum class WorkBookType(val description: String) {
    SUBJECTIVE("주관식"),
    DESCRIPTIVE("서술형"),
    ;

    companion object {
        fun create(workbookType: String): WorkBookType =
            entries.firstOrNull { it.description == workbookType }
                ?: throw BaseException(ApiErrorCode.WORK_BOOK_TYPE_INVALID)
    }
}
