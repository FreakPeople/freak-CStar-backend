package yjh.cstar.quiz.application.port.QuizFilter

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import yjh.cstar.quiz.domain.Quiz

interface QuizFilter {
    fun filter(memberId: Long, pageable: Pageable): Page<Quiz>
}
