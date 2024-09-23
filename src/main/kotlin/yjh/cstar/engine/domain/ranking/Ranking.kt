package yjh.cstar.engine.domain.ranking

// <String, Int> "player:1", 10
class Ranking(
    private val ranking: LinkedHashMap<String, Int>
) {

    companion object {
        fun of(ranking: List<Pair<String?, Double?>>): Ranking {
            val mapOfRanking = ranking
                .filter { it.first != null && it.second != null }
                .associate { it.first!! to it.second!!.toInt() }
                .toMap(LinkedHashMap())
            return Ranking(mapOfRanking)
        }
    }

    fun getRanking() = ranking.toList()
        .sortedByDescending { it.second }
        .toMap(LinkedHashMap())
}
