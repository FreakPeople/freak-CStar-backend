package yjh.cstar.play.domain.quiz

data class QuizDto(val id: Long, val question: String, val answer: String)

fun QuizDto.toModel(): Quiz = Quiz(
    id = this.id,
    question = this.question,
    answer = this.answer
)
