package yjh.cstar.play.domain.player

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException

class Players(val players: Map<Long, String>) {

    init {
        require(players.isNotEmpty()) {
            throw BaseException(BaseErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    companion object {
        fun of(players: Map<Long, String>) = Players(players)
    }

    fun getNickname(winnerId: Long) = players[winnerId]
        ?: throw BaseException(BaseErrorCode.INTERNAL_SERVER_ERROR)

    fun getPlayerIds(): List<Long> = players.keys.toList()
}
