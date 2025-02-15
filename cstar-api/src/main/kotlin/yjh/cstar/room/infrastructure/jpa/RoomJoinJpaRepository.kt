package yjh.cstar.room.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RoomJoinJpaRepository : JpaRepository<RoomJoinEntity, Long> {

    @Query(
        value = """
            SELECT *
            FROM room_join
            WHERE room_id = :roomId
            ORDER BY joined_at DESC
            LIMIT :currentCapacity
        """,
        nativeQuery = true
    )
    fun findCurrParticipant(
        @Param("roomId") roomId: Long,
        @Param("currentCapacity") currentCapacity: Int,
    ): List<RoomJoinEntity>
}
