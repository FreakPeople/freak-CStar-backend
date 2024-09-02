package yjh.cstar.quiz.application.port.QuizFilter

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import yjh.cstar.quiz.application.port.QuizRepository
import yjh.cstar.quiz.domain.Quiz

class AttemptedQuizFilter(
    private val quizRepository: QuizRepository,
) : QuizFilter {
    override fun filter(memberId: Long, pageable: Pageable): Page<Quiz> {
        return quizRepository.findAllAttemptedByMember(memberId, pageable)
    }
}
