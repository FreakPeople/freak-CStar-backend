package yjh.cstar.category.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException

enum class CategoryType(val id: Long, val description: String) {
    ALGORITHM(1L, "알고리즘"),
    DATABASE(2L, "데이터베이스"),
    DESIGN_PATTERN(3L, "디자인패턴"),
    NETWORK(4L, "네트워크"),
    OPERATING_SYSTEM(5L, "운영체제"),
    ;

    companion object {
        fun create(quizCategoryId: Long): CategoryType =
            CategoryType.entries.firstOrNull { it.id == quizCategoryId }
                ?: throw BaseException(BaseErrorCode.QUIZ_CATEGORY_INVALID)
    }
}
