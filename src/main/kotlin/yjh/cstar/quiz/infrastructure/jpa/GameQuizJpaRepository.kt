package yjh.cstar.quiz.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface GameQuizJpaRepository : JpaRepository<GameQuizEntity, GameQuizId>
