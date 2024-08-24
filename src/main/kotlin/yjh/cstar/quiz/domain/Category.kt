package yjh.cstar.quiz.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException

enum class Category(val description: String) {
    NETWORK("네트워크"),
    ALGORITHM("알고리즘"),
    OPERATING_SYSTEM("운영체제"),
    DATABASE("데이터베이스"),
    DESIGN_PATTERN("디자인패턴"),
    ;

    companion object {
        fun create(quizCategory: String): Category =
            entries.firstOrNull { it.description == quizCategory }
                ?: throw BaseException(BaseErrorCode.QUIZ_CATEGORY_INVALID)
    }
}
