package yjh.cstar.game.application

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import yjh.cstar.quiz.domain.Quiz

@Service
class GameEngineService {
    @Async("GameEngineThreadPool")
    fun start(quizzes: List<Quiz>, roomId: Long, playerId: Long?) {
        // 게임 로직 구현 예정
    }

}
