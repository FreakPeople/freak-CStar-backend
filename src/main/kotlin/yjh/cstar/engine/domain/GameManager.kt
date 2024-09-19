package yjh.cstar.engine.domain

class GameManager(
    val players: Players,
    val quizzes: Quizzes,
    val roomId: Long,
) {
    val startTime = StartTime()
}
