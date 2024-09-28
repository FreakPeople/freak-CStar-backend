package yjh.cstar.play.domain.quiz

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("[Domain 테스트] PlayerAnswer")
class PlayerAnswerTest {

    @Test
    fun `같은 정답인지 비교 테스트`() {
        // given
        val playerAnswer = PlayerAnswer(
            answer = "정답",
            quizId = 1L,
            roomId = 1L,
            playerId = 1L,
            nickname = "닉네임"
        )
        val quiz = Quiz(1L, "질문", "정답")

        // when
        val isCorrect = playerAnswer.isCorrect(quiz)

        // then
        assertTrue(isCorrect)
    }

    @Test
    fun `같은 정답이랑 다른지 비교 테스트`() {
        // given
        val playerAnswer = PlayerAnswer(
            answer = "정답",
            quizId = 1L,
            roomId = 1L,
            playerId = 1L,
            nickname = "닉네임"
        )
        val quiz = Quiz(1L, "질문", "잘못된 정답")

        // when
        val isCorrect = playerAnswer.isCorrect(quiz)

        // then
        assertFalse(isCorrect)
    }
}
