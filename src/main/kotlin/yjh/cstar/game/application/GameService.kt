package yjh.cstar.game.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.game.domain.GameStartCommand
import yjh.cstar.member.application.MemberService
import yjh.cstar.play.application.GamePlayService
import yjh.cstar.play.application.request.QuizDto
import yjh.cstar.quiz.application.QuizService
import yjh.cstar.room.application.RoomService

@Transactional(readOnly = true)
@Service
class GameService(
    val memberService: MemberService,
    val roomService: RoomService,
    val quizService: QuizService,
    val gamePlayService: GamePlayService,
) {
    @Transactional
    fun start(command: GameStartCommand) {
        roomService.startGame(command.roomId)

        val playerInfo: Map<Long, String> = getParticipantPlayerInfo(command)

        val randomQuizData: List<QuizDto> = getRandomQuizData(command)

        gamePlayService.start(playerInfo, randomQuizData, command.roomId, command.quizCategoryId)
    }

    private fun getRandomQuizData(command: GameStartCommand) =
        quizService.getRandomQuizzes(command.quizCategoryId, command.totalQuestions)
            .map { QuizDto(it.id, it.question, it.answer) }

    private fun getParticipantPlayerInfo(command: GameStartCommand): Map<Long, String> {
        val playerIds: List<Long> = roomService.retrieveCurrParticipant(command.roomId)
        val players: Map<Long, String> = memberService.retrieveAll(playerIds)
            .associate { it.id to it.nickname }
        return players
    }
}
