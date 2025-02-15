package yjh.cstar.game.domain

class GameStartCommand(
    val roomId: Long,
    val quizCategoryId: Long,
    val totalQuestions: Int,
)
