package yjh.cstar.game.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.game.domain.GameStartCommand
import yjh.cstar.quiz.application.QuizService
import yjh.cstar.room.application.RoomService

@Transactional(readOnly = true)
@Service
class GameService(
    val roomService: RoomService,
    val quizService: QuizService,
    val gameEngineService: GameEngineService,
) {
    @Transactional
    fun start(command: GameStartCommand) {
        roomService.startGame(command.roomId)

        val quizzes = quizService.getQuizzes(command.quizCategory, command.totalQuestions)

        // 실제 퀴즈 실행하는 비동기 메서드
        gameEngineService.start(quizzes, command.roomId)
    }
}
