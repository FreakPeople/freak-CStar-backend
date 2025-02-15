package yjh.cstar.play.domain.quiz

class Quiz(
    val id: Long,
    val question: String,
    val answer: String,
) {
    companion object {
        fun of(id: Long, question: String, answer: String): Quiz =
            Quiz(id, question, answer)
    }

    fun isSameAnswer(playerAnswer: String): Boolean =
        normalize(this.answer) == normalize(playerAnswer)

    private fun normalize(str: String): String =
        str.replace("\\s".toRegex(), "").lowercase()
}
