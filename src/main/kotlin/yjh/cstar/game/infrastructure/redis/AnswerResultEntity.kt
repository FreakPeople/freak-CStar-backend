package yjh.cstar.game.infrastructure.redis

import yjh.cstar.game.domain.AnswerResult

class AnswerResultEntity(
    val answer: String,
    val quizId: Long,
    val roomId: Long,
    val playerId: Long,
) {

    companion object {
        fun from(answerResult: AnswerResult): AnswerResultEntity {
            return AnswerResultEntity(
                answer = answerResult.answer,
                quizId = answerResult.quizId,
                roomId = answerResult.roomId,
                playerId = answerResult.playerId
            )
        }
    }

    fun toModel(): AnswerResult {
        return AnswerResult(
            answer = this.answer,
            quizId = this.quizId,
            roomId = this.roomId,
            playerId = this.playerId
        )
    }
}
