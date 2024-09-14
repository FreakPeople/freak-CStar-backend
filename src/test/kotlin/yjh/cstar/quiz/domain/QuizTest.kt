package yjh.cstar.quiz.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import yjh.cstar.category.domain.CategoryType
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("[Domain 테스트] Quiz")
class QuizTest {

    @Test
    fun `퀴즈 생성 테스트`() {
        // given
        val command = QuizCreateCommand(
            question = "question",
            answer = "answer",
            categoryId = CategoryType.DATABASE.id
        )
        val writerId = 1L

        // when
        val quiz = assertDoesNotThrow { Quiz.create(command, writerId) }

        // then
        assertNotNull(quiz)
        assertEquals(1L, quiz.writerId)
        assertEquals("question", quiz.question)
        assertEquals("answer", quiz.answer)
        assertEquals(CategoryType.DATABASE.id, quiz.categoryId)
    }
}
