package yjh.cstar.game.domain

class GameStartCommand(
    val roomId: Long,
    val quizCategory: String,
    val totalQuestions: Int,
)
