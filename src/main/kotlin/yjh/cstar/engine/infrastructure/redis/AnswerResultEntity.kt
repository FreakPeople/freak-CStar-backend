package yjh.cstar.engine.infrastructure.redis

import yjh.cstar.engine.domain.quiz.PlayerAnswer
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

    fun toModel2(): PlayerAnswer {
        return PlayerAnswer(
            roomId = this.roomId,
            playerId = this.playerId,
            quizId = this.quizId,
            playerAnswer = answer
        )
    }
}
