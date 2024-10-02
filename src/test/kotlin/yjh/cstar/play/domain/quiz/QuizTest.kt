package yjh.cstar.play.domain.quiz

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

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

    @ParameterizedTest
    @CsvSource(
        "정답, 정 답,true",
        "정   답, 정답, true",
        "abc, ABC, true" // 공백이 포함된 경우 등 다양한 케이스도 추가 가능
    )
    fun `같은 정답을 가지고 있는지 테스트`(answer: String, playerAnswer: String, expected: Boolean) {
        // given
        val quiz = Quiz(1L, "question", answer)

        // when
        val isSameAnswer = quiz.isSameAnswer(playerAnswer)

        // then
        assertEquals(expected, isSameAnswer)
    }

    @Test
    fun `다른 정답을 가지고 있는지 테스트`() {
        // given
        val answer = "정답"
        val quiz = Quiz(1L, "질문", answer)

        // when
        val isSameAnswer = quiz.isSameAnswer("잘못된 정답")

        // then
        assertFalse(isSameAnswer)
    }
}
