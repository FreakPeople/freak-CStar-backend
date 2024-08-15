package yjh.cstar.quiz.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface QuizJpaRepository : JpaRepository<QuizEntity, Long>
