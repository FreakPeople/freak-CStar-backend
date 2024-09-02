package yjh.cstar.quiz.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException

enum class QuizFilterType(val description: String) {
    CREATED("created"),
    ATTEMPTED("attempted"),
    CORRECT("correct"),
    ;

    companion object {
        fun create(quizFilter: String): QuizFilterType =
            entries.firstOrNull { it.description == quizFilter }
                ?: throw BaseException(BaseErrorCode.QUIZ_FILTER_INVALID)
    }
}
