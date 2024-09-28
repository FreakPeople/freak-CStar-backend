package yjh.cstar.play.domain.quiz

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("[Domain 테스트] Quiz")
class QuizTest {

    @Test
    fun `퀴즈 생성 테스트`() {
        // given
        val quizId = 1L
        val question = "질문"
        val answer = "정답"

        // when & then
        assertDoesNotThrow { Quiz.of(quizId, question, answer) }
    }

    @Test
    fun `같은 정답을 가지고 있는지 테스트`() {
        // given
        val quizId = 1L
        val question = "질문"
        val answer = "정답"
        val quiz = Quiz(quizId, question, answer)

        // when
        val isSameAnswer = quiz.isSameAnswer("정답")

        // then
        assertTrue(isSameAnswer)
    }

    @Test
    fun `다른 정답을 가지고 있는지 테스트`() {
        // given
        val quizId = 1L
        val question = "질문"
        val answer = "정답"
        val quiz = Quiz(quizId, question, answer)

        // when
        val isSameAnswer = quiz.isSameAnswer("잘못된 정답")

        // then
        assertFalse(isSameAnswer)
    }
}
