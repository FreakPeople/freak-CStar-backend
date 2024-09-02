package yjh.cstar.quiz.application.port.QuizFilter

import org.springframework.stereotype.Component
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.quiz.application.port.QuizRepository
import yjh.cstar.quiz.domain.QuizFilterType

@Component
class QuizFilterFactory(
    private val quizRepository: QuizRepository,
) {
    fun filterType(quizFilterType: String): QuizFilter {
        return when (QuizFilterType.create(quizFilterType)) {
            QuizFilterType.CREATED -> CreatedQuizFilter(quizRepository)
            QuizFilterType.ATTEMPTED -> AttemptedQuizFilter(quizRepository)
            else -> throw BaseException(BaseErrorCode.QUIZ_FILTER_INVALID)
        }
    }
}
