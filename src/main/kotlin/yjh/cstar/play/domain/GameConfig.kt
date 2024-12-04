package yjh.cstar.play.domain

import yjh.cstar.game.application.GameResultService
import yjh.cstar.play.application.port.AnswerProvider
import yjh.cstar.play.application.port.GameNotifier
import yjh.cstar.play.application.port.RankingHandler
import yjh.cstar.room.application.RoomService

data class GameConfig(
    val answerProvider: AnswerProvider,
    val gameNotifier: GameNotifier,
    val rankingHandler: RankingHandler,
    val gameResultService: GameResultService,
    val roomService: RoomService,
)
