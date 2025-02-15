package yjh.cstar.quiz.domain

import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.exception.BaseException

enum class QuizFilterType(val description: String) {
    CREATED("created"),
    ATTEMPTED("attempted"),
    CORRECT("correct"),
    ;

    companion object {
        fun create(quizFilter: String): QuizFilterType =
            entries.firstOrNull { it.description == quizFilter }
                ?: throw BaseException(ApiErrorCode.QUIZ_FILTER_INVALID)
    }
}
