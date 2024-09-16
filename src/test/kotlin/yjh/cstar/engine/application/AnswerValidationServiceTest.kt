package yjh.cstar.engine.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import yjh.cstar.IntegrationTest
import kotlin.test.assertTrue

@DisplayName("[Application 테스트] AnswerValidationService")
class AnswerValidationServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var answerValidationService: AnswerValidationService

    @Test
    fun `플레이어 정답 확인 테스트`() {
        // given
        val submittedAnswer = "정답"
        val correctAnswer = "정답"

        // when
        val result = answerValidationService.validateAnswer(submittedAnswer, correctAnswer)

        // then
        assertTrue(result)
    }

    @Test
    fun `플레이어 정답 확인 테스트 - 띄어쓰기에 관계없이 정답 처리`() {
        // given
        val submittedAnswer = "정 답"
        val correctAnswer = " 정답 "

        // when
        val result = answerValidationService.validateAnswer(submittedAnswer, correctAnswer)

        // then
        assertTrue(result)
    }

    @Test
    fun `플레이어 정답 확인 테스트 - 대소문자에 관계없이 정답 처리`() {
        // given
        val submittedAnswer = "answer"
        val correctAnswer = "ANSWER"

        // when
        val result = answerValidationService.validateAnswer(submittedAnswer, correctAnswer)

        // then
        assertTrue(result)
    }
}
