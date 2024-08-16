package yjh.cstar.room.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface RoomJpaRepository : JpaRepository<RoomEntity, Long>
