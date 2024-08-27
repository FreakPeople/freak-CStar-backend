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
    fun start(command: GameStartCommand, playerId: Long) {
        // 방이 존재 하는지 확인
        val room = roomService.retrieve(command.roomId)

        // 방 게임 시작
        room.startGame()

        // command 안에 category 기준으로 10개의 랜덤 문제를 가져온다.
        val quizzes = quizService.getQuizzes(command.quizCategory, command.totalQuestions)

        // 실제 퀴즈 제공해주는 별도의 스레드 생성
        gameEngineService.start(quizzes, room.id, playerId)
    }
}
