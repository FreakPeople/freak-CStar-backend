package yjh.cstar.game.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface GameResultJpaRepository : JpaRepository<GameResultEntity, Long>
