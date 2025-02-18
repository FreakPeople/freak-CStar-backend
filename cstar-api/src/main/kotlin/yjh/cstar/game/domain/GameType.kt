package yjh.cstar.game.domain

import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.exception.BaseException

enum class GameType(val description: String) {
    SINGLE("1인플레이"),
    MULTI("다인플레이"),
    ;

    companion object {
        fun create(gameType: String): GameType =
            entries.firstOrNull { it.description == gameType }
                ?: throw BaseException(ApiErrorCode.GAME_TYPE_INVALID)
    }
}
