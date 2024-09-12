package yjh.cstar.engine.infrastructure.redis

import yjh.cstar.game.domain.AnswerResult

class AnswerResultEntity(
    val answer: String,
    val quizId: Long,
    val roomId: Long,
    val playerId: Long,
    val nickname: String,
) {

    companion object {
        fun from(answerResult: AnswerResult): AnswerResultEntity {
            return AnswerResultEntity(
                answer = answerResult.answer,
                quizId = answerResult.quizId,
                roomId = answerResult.roomId,
                playerId = answerResult.playerId,
                nickname = answerResult.nickname
            )
        }
    }

    fun toModel(): AnswerResult {
        return AnswerResult(
            answer = this.answer,
            quizId = this.quizId,
            roomId = this.roomId,
            playerId = this.playerId,
            nickname = this.nickname
        )
    }
}
