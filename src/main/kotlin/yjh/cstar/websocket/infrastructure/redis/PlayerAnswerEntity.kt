package yjh.cstar.websocket.infrastructure.redis

import yjh.cstar.websocket.domain.PlayerAnswer

class PlayerAnswerEntity(
    val answer: String,
    val quizId: Long,
    val roomId: Long,
    val playerId: Long,
    val nickname: String,
) {

    companion object {
        fun from(playerAnswer: PlayerAnswer): PlayerAnswerEntity {
            return PlayerAnswerEntity(
                answer = playerAnswer.answer,
                quizId = playerAnswer.quizId,
                roomId = playerAnswer.roomId,
                playerId = playerAnswer.playerId,
                nickname = playerAnswer.nickname
            )
        }
    }

    fun toModel(): PlayerAnswer {
        return PlayerAnswer(
            answer = this.answer,
            quizId = this.quizId,
            roomId = this.roomId,
            playerId = this.playerId,
            nickname = this.nickname
        )
    }
}
