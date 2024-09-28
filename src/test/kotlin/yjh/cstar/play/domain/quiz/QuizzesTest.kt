package yjh.cstar.play.domain.quiz

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

@DisplayName("[Domain 테스트] Quizzes")
class QuizzesTest {

    @Test
    fun `퀴즈목록 생성 테스트`() {
        // given
        val quizDtos = listOf(
            QuizDto(1L, "question1", "answer1"),
            QuizDto(2L, "question2", "answer2"),
            QuizDto(3L, "question3", "answer3")
        )

        // when & then
        assertDoesNotThrow { Quizzes.of(quizDtos) }
    }

    @Test
    fun `퀴즈목록 조회 테스트`() {
        // given
        val quizDtos = listOf(
            QuizDto(1L, "question1", "answer1"),
            QuizDto(2L, "question2", "answer2"),
            QuizDto(3L, "question3", "answer3")
        )
        val quizzes = Quizzes.of(quizDtos)

        // when
        val quizList = quizzes.getQuizList()

        // then
        assertEquals(3, quizList.size)
    }

    @Test
    fun `퀴즈목록 개수 조회 테스트`() {
        // given
        val quizDtos = listOf(
            QuizDto(1L, "question1", "answer1"),
            QuizDto(2L, "question2", "answer2"),
            QuizDto(3L, "question3", "answer3")
        )
        val quizzes = Quizzes.of(quizDtos)

        // when
        val size = quizzes.getSize()

        // then
        assertEquals(3, size)
    }
}
