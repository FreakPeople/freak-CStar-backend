package yjh.cstar.engine.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import yjh.cstar.engine.application.port.GameAnswerPollRepository
import yjh.cstar.engine.domain.GameManager
import yjh.cstar.engine.domain.Players
import yjh.cstar.engine.domain.Quizzes
import yjh.cstar.game.application.GameResultService
import yjh.cstar.quiz.domain.Quiz
import yjh.cstar.util.RedisUtil
import yjh.cstar.websocket.application.BroadCastService

private val logger = KotlinLogging.logger {}

@Service
class GameEngineService(
    private val gameAnswerPollRepository: GameAnswerPollRepository,
    private val gameResultService: GameResultService,
    private val rankingService: RankingService,
    private val answerValidationService: AnswerValidationService,
    private val broadCastService: BroadCastService,
    private val redisRankingService: RedisRankingService,
    private val redisUtil: RedisUtil,
) {

    @Async("GameEngineThreadPool")
    fun start(players: List<Long>, quizzes: List<Quiz>, roomId: Long) {
        logger.info { "[INFO] 게임 엔진 스레드 시작 - roomId : $roomId" }

        try {
            validate(players, quizzes, roomId)

            TODO("아래의 많은 의존성들을 어떻게 분리할 수 있을지 고민")
            // StoreService, InputService, OutputService 로 나누고, 퍼사드 활용?
            val gameManager = GameManager(
                Players(players), Quizzes(quizzes), roomId, quizzes.first().categoryId,
                gameAnswerPollRepository,
                gameResultService,
                rankingService,
                answerValidationService,
                broadCastService,
                redisRankingService,
                redisUtil
            )

            gameManager.start()
        } catch (e: Exception) {
            logger.error { e.message }
        } finally {
            TODO("성공/실패 여부에 따른 마무리 로직 추가")
            logger.info { "[INFO] 게임 엔진 스레드 종료 - roomId : $roomId" }
        }
    }

    private fun validate(players: List<Long>, quizzes: List<Quiz>, roomId: Long) {
        require(players.size != 0) { "[ERROR] players size is 0" }
        require(quizzes.size != 0) { "[ERROR] quizzes size is 0" }
        require(roomId > 0) { "[ERROR] invalid roomId" }
    }
}
