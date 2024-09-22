package yjh.cstar.engine.domain.player

class Players(val players: Map<Long, String>) {

    companion object {
        fun of(players: Map<Long, String>) = Players(players)
    }

    fun getNickname(winnerId: Long) = players[winnerId] ?: "None"

    fun getPlayerIds(): List<Long> = players.keys.toList()
}
