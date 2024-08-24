package yjh.cstar.quiz.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import yjh.cstar.quiz.domain.Category

interface QuizJpaRepository : JpaRepository<QuizEntity, Long> {
    @Query(
        value = """
            SELECT *
            FROM quiz
            WHERE category = :quizCategory
                AND deleted_at IS NULL
            ORDER BY created_at DESC
            LIMIT :totalQuestions
        """,
        nativeQuery = true
    )
    fun getQuizzes(
        @Param("quizCategory") quizCategory: Category,
        @Param("totalQuestions") totalQuestions: Int,
    ): List<QuizEntity>
}
