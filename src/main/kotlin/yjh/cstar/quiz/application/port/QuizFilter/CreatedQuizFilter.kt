package yjh.cstar.quiz.application.port.QuizFilter

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import yjh.cstar.quiz.application.port.QuizRepository
import yjh.cstar.quiz.domain.Quiz

class CreatedQuizFilter(
    private val quizRepository: QuizRepository,
) : QuizFilter {
    override fun filter(writerId: Long, pageable: Pageable): Page<Quiz> {
        return quizRepository.findAllCreatedByMember(writerId, pageable)
    }
}
