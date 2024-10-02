package yjh.cstar.quiz.infrastructure.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface QuizJpaRepository : JpaRepository<QuizEntity, Long> {
    @Query(
        value = """
            SELECT *
            FROM quiz
            WHERE category_id = :quizCategoryId
                AND deleted_at IS NULL
            ORDER BY created_at DESC
            LIMIT :totalQuestions
        """,
        nativeQuery = true
    )
    fun getRandomQuizzes(
        @Param("quizCategoryId") quizCategoryId: Long,
        @Param("totalQuestions") totalQuestions: Int,
    ): List<QuizEntity>

    @Query("SELECT q FROM QuizEntity q WHERE q.deletedAt IS NULL AND q.categoryId = :quizCategoryId")
    fun findAllByCategory(@Param("quizCategoryId") quizCategoryId: Long, pageable: Pageable): Page<QuizEntity>

    @Query("SELECT q FROM QuizEntity q WHERE q.deletedAt IS NULL AND q.writerId = :memberId")
    fun findAllCreatedByMember(@Param("memberId") writerId: Long, pageable: Pageable): Page<QuizEntity>

    @Query(
        value = """
        SELECT q.*
        FROM quiz q
        WHERE q.deleted_at IS NULL
          AND q.quiz_id IN (
              SELECT gq.quiz_id
              FROM game_quiz gq
              JOIN member_game_result mgr ON mgr.game_id = gq.game_id
              WHERE mgr.member_id = :memberId
                AND mgr.total_count > 0
          )
    """,
        nativeQuery = true
    )
    fun findAllAttemptedByMember(
        @Param("memberId") memberId: Long,
        pageable: Pageable,
    ): Page<QuizEntity>
}
