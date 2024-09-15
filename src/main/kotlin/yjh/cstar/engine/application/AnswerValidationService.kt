package yjh.cstar.engine.application

import org.springframework.stereotype.Service

@Service
class AnswerValidationService {
    fun validateAnswer(submittedAnswer: String, correctAnswer: String): Boolean {
        return submittedAnswer.replace(" ", "")
            .equals(correctAnswer.replace(" ", ""), ignoreCase = true)
    }
}
