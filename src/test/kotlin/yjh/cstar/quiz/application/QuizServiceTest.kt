package yjh.cstar.quiz.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import yjh.cstar.IntegrationTest
import yjh.cstar.common.BaseException
import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.domain.QuizCreateCommand
import yjh.cstar.quiz.infrastructure.jpa.QuizEntity
import yjh.cstar.quiz.infrastructure.jpa.QuizJpaRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DisplayName("[Application 테스트] QuizService")
class QuizServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var quizService: QuizService

    @Autowired
    private lateinit var quizJpaRepository: QuizJpaRepository

    @Test
    fun `퀴즈 생성 테스트`() {
        // given
        val command = QuizCreateCommand(
            question = "question",
            answer = "answer",
            category = Category.ALGORITHM
        )
        val writerId = 1L

        // when
        val quizId = quizService.create(command, writerId)

        // then
        assertTrue(quizId > 0L)
        val quiz = quizJpaRepository.findByIdOrNull(quizId)?.toModel()

        assertNotNull(quiz)
        assertEquals("question", quiz.question)
        assertEquals("answer", quiz.answer)
        assertEquals(Category.ALGORITHM, quiz.category)
        assertNotNull(quiz.createdAt)
        assertNotNull(quiz.updatedAt)
        assertNull(quiz.deletedAt)
    }

    @Test
    fun `퀴즈 문제 조회 테스트`() {
        // given
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제1",
                answer = "정답1",
                category = Category.ALGORITHM,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제2",
                answer = "정답2",
                category = Category.ALGORITHM,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제3",
                answer = "정답3",
                category = Category.ALGORITHM,
                createdAt = null,
                updatedAt = null
            )
        )
        val quizCategory = "알고리즘"
        val totalQuestions = 2

        // when
        val quizzes = quizService.getQuizzes(
            quizCategory = quizCategory,
            totalQuestions = totalQuestions
        )

        // then
        assertEquals(2, quizzes.size)
    }

    @Test
    fun `퀴즈 조회시 잘못된 카테고리 이름 입력`() {
        // given
        val quizCategory = "알고리x"
        val totalQuestions = 2

        // when
        val exception = assertThrows<BaseException> {
            quizService.getQuizzes(
                quizCategory = quizCategory,
                totalQuestions = totalQuestions
            )
        }

        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.baseErrorCode.httpStatus)
    }
}
