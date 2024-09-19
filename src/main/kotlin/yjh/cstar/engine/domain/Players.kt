package yjh.cstar.engine.domain

class Players(
    val ids: List<Long>,
    val players: MutableMap<Long, Player> = ids.associateTo(mutableMapOf()) {
            id ->
        id to Player(id, null)
    },
) {
    init {
        // 정합성 검사
    }

    fun findWinner(playerId: Long): Player? {
        return players[playerId]
    }

    fun addNickName(playerId: Long, nickname: String) {
        players[playerId] = Player(playerId, nickname)
    }

    fun getNickname(playerId: Long): String? {
        return players[playerId]?.nickname
    }
}
