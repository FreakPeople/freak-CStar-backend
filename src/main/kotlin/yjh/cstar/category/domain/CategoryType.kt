package yjh.cstar.category.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException

enum class CategoryType(val description: String) {
    ALGORITHM("알고리즘"),
    DATABASE("데이터베이스"),
    DESIGN_PATTERN("디자인패턴"),
    NETWORK("네트워크"),
    OPERATING_SYSTEM("운영체제"),
    ;
    companion object {
        fun create(quizCategory: String): CategoryType =
            entries.firstOrNull { it.description == quizCategory }
                ?: throw BaseException(BaseErrorCode.QUIZ_CATEGORY_INVALID)
    }
}
