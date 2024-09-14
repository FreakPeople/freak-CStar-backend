package yjh.cstar.quiz.application

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import yjh.cstar.IntegrationTest
import yjh.cstar.category.domain.CategoryType
import yjh.cstar.common.BaseException
import yjh.cstar.game.infrastructure.jpa.GameEntity
import yjh.cstar.game.infrastructure.jpa.GameJpaRepository
import yjh.cstar.game.infrastructure.jpa.GameResultEntity
import yjh.cstar.game.infrastructure.jpa.GameResultJpaRepository
import yjh.cstar.quiz.domain.QuizCreateCommand
import yjh.cstar.quiz.infrastructure.jpa.GameQuizEntity
import yjh.cstar.quiz.infrastructure.jpa.GameQuizId
import yjh.cstar.quiz.infrastructure.jpa.GameQuizJpaRepository
import yjh.cstar.quiz.infrastructure.jpa.QuizEntity
import yjh.cstar.quiz.infrastructure.jpa.QuizJpaRepository
import java.time.LocalDateTime
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

    @Autowired
    private lateinit var gameJpaRepository: GameJpaRepository

    @Autowired
    private lateinit var gameResultJpaRepository: GameResultJpaRepository

    @Autowired
    private lateinit var gameQuizJpaRepository: GameQuizJpaRepository

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
        val quizId = quizService.create(command, writerId)

        // then
        assertTrue(quizId > 0L)
        val quiz = quizJpaRepository.findByIdOrNull(quizId)?.toModel()

        assertNotNull(quiz)
        assertEquals("question", quiz.question)
        assertEquals("answer", quiz.answer)
        assertEquals(CategoryType.DATABASE.id, quiz.categoryId)
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
                categoryId = CategoryType.DATABASE.id,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제2",
                answer = "정답2",
                categoryId = CategoryType.DATABASE.id,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제3",
                answer = "정답3",
                categoryId = CategoryType.DATABASE.id,
                createdAt = null,
                updatedAt = null
            )
        )
        val quizCategoryId = 2L
        val totalQuestions = 2

        // when
        val quizzes = quizService.getQuizzes(
            quizCategoryId = quizCategoryId,
            totalQuestions = totalQuestions
        )

        // then
        assertEquals(2, quizzes.size)
    }

    @Test
    fun `퀴즈 조회시 잘못된 카테고리 이름 입력`() {
        // given
        val quizCategoryId = 999L
        val totalQuestions = 2

        // when
        val exception = assertThrows<BaseException> {
            quizService.getQuizzes(
                quizCategoryId = quizCategoryId,
                totalQuestions = totalQuestions
            )
        }

        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.baseErrorCode.httpStatus)
    }

    @Test
    fun `퀴즈 카테고리별로 퀴즈를 조회하는 테스트`() {
        // given
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제1",
                answer = "정답1",
                categoryId = CategoryType.DATABASE.id,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 2L,
                question = "문제2",
                answer = "정답2",
                categoryId = CategoryType.DATABASE.id,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제3",
                answer = "정답3",
                categoryId = CategoryType.NETWORK.id,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제4",
                answer = "정답4",
                categoryId = CategoryType.ALGORITHM.id,
                createdAt = null,
                updatedAt = null
            )
        )

        // when
        val page1 = quizService.retrieveAllByCategory(
            quizCategoryId = 2L,
            pageable = PageRequest.of(0, 10)
        )

        // then
        assertEquals(2, page1.content.size)
    }

    @Test
    fun `퀴즈 카테고리별로 퀴즈를 조회 할 때, 잘못된 카테고리 이름 입력 시 BAD_REQUEST 에러 발생 테스트`() {
        // given
        val quizCategoryId = 999L

        // when
        val exception = assertThrows<BaseException> {
            quizService.retrieveAllByCategory(
                quizCategoryId = quizCategoryId,
                pageable = PageRequest.of(0, 10)
            )
        }

        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.baseErrorCode.httpStatus)
    }

    @Test
    fun `특정 플레이어가 생성한 퀴즈를 조회하는 테스트`() {
        // given
        val memberId = 1L
        val quizFilter = "created"

        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제11",
                answer = "정답11",
                categoryId = CategoryType.NETWORK.id,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "문제22",
                answer = "정답22",
                categoryId = CategoryType.DATABASE.id,
                createdAt = null,
                updatedAt = null
            )
        )
        quizJpaRepository.save(
            QuizEntity(
                writerId = 2L,
                question = "문제33",
                answer = "정답33",
                categoryId = CategoryType.ALGORITHM.id,
                createdAt = null,
                updatedAt = null
            )
        )

        // when
        val pageByCreated = quizService.retrieveAllByQuizFilterType(
            memberId,
            quizFilter,
            pageable = PageRequest.of(0, 10)
        )

        // then
        assertEquals(2, pageByCreated.content.size)
    }

    @Test
    fun `특정 플레이어가 생성한 퀴즈를 조회할 때 지원하지 않는 퀴즈 필터 입력 시 BAD_REQUEST 에러 발생 테스트`() {
        // given
        val memberId = 2L
        val quizFilter = "corrected"

        quizJpaRepository.save(
            QuizEntity(
                writerId = 2L,
                question = "문제33",
                answer = "정답33",
                categoryId = CategoryType.ALGORITHM.id,
                createdAt = null,
                updatedAt = null
            )
        )

        // when
        val exception = assertThrows<BaseException> {
            quizService.retrieveAllByQuizFilterType(
                memberId,
                quizFilter,
                pageable = PageRequest.of(0, 10)
            )
        }

        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.baseErrorCode.httpStatus)
    }

    @Test
    fun `특정 플레이어가 시도한 퀴즈(푼 문제 + 못 푼 문제)를 조회하는 테스트`() {
        // given
        val memberId = 3L
        val quizFilter = "attempted"

        // 퀴즈 생성
        val quiz1 = quizJpaRepository.save(
            QuizEntity(
                id = 1L,
                writerId = 1L,
                question = "문제111",
                answer = "정답111",
                categoryId = CategoryType.ALGORITHM.id,
                createdAt = null,
                updatedAt = null
            )
        ).toModel()

        val quiz2 = quizJpaRepository.save(
            QuizEntity(
                id = 2L,
                writerId = 1L,
                question = "문제222",
                answer = "정답222",
                categoryId = CategoryType.ALGORITHM.id,
                createdAt = null,
                updatedAt = null
            )
        ).toModel()

        val quiz3 = quizJpaRepository.save(
            QuizEntity(
                id = 3L,
                writerId = 1L,
                question = "문제333",
                answer = "정답333",
                categoryId = CategoryType.ALGORITHM.id,
                createdAt = null,
                updatedAt = null
            )
        ).toModel()

        // 게임에 참가
        val savedGame = gameJpaRepository.save(
            GameEntity(
                roomId = 1L,
                winnerId = 2L,
                totalQuizCount = 3,
                categoryId = 2L,
                startedAt = LocalDateTime.now(),
                createdAt = null,
                updatedAt = null
            )
        ).toModel()

        gameResultJpaRepository.save(
            GameResultEntity(
                gameId = savedGame.id,
                playerId = memberId,
                totalCount = 3,
                correctCount = 2,
                ranking = 1,
                createdAt = null,
                updatedAt = null
            )
        ).toModel().id

        gameQuizJpaRepository.saveAll(
            listOf(
                GameQuizEntity(id = GameQuizId(gameId = savedGame.id, quizId = quiz1.id)),
                GameQuizEntity(id = GameQuizId(gameId = savedGame.id, quizId = quiz2.id)),
                GameQuizEntity(id = GameQuizId(gameId = savedGame.id, quizId = quiz3.id))
            )
        )

        // when
        val pageByCreated = quizService.retrieveAllByQuizFilterType(
            memberId,
            quizFilter,
            pageable = PageRequest.of(0, 10)
        )

        // then
        assertEquals(3, pageByCreated.content.size)
    }
}
