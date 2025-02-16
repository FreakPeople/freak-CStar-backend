package yjh.cstar.room.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RoomJpaRepository : JpaRepository<RoomEntity, Long> {

    @Query("SELECT r FROM RoomEntity r WHERE r.deletedAt IS NULL AND r.id = :id")
    fun findByIdOrNull(@Param("id") id: Long): RoomEntity?
}
