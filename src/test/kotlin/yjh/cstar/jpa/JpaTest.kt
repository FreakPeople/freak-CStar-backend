package yjh.cstar.jpa

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import yjh.cstar.config.JpaConfig
import yjh.cstar.game.infrastructure.jpa.GameEntity
import yjh.cstar.game.infrastructure.jpa.GameJpaRepository
import yjh.cstar.game.infrastructure.jpa.GameResultEntity
import yjh.cstar.game.infrastructure.jpa.GameResultJpaRepository
import yjh.cstar.member.infrastructure.jpa.MemberEntity
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository
import yjh.cstar.quiz.domain.Category
import yjh.cstar.quiz.infrastructure.jpa.GameQuizEntity
import yjh.cstar.quiz.infrastructure.jpa.GameQuizId
import yjh.cstar.quiz.infrastructure.jpa.GameQuizJpaRepository
import yjh.cstar.quiz.infrastructure.jpa.QuizEntity
import yjh.cstar.quiz.infrastructure.jpa.QuizJpaRepository
import yjh.cstar.room.domain.RoomStatus
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJoinEntity
import yjh.cstar.room.infrastructure.jpa.RoomJoinJpaRepository
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ActiveProfiles("local-test")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig::class)
@DataJpaTest
class JpaTest {

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Autowired
    private lateinit var roomJpaRepository: RoomJpaRepository

    @Autowired
    private lateinit var roomJoinJpaRepository: RoomJoinJpaRepository

    @Autowired
    private lateinit var gameJpaRepository: GameJpaRepository

    @Autowired
    private lateinit var gameResultJpaRepository: GameResultJpaRepository

    @Autowired
    private lateinit var quizJpaRepository: QuizJpaRepository

    @Autowired
    private lateinit var gameQuizJpaRepository: GameQuizJpaRepository

    @DisplayName("Member Entity 연결 테스트")
    @Test
    fun test1() {
        // given
        memberJpaRepository.save(
            MemberEntity(
                email = "email",
                password = "password",
                nickname = "nickname",
                createdAt = null,
                updatedAt = null
            )
        )

        // when
        val members = memberJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(members) },
            { assertEquals(1, members.size) }
        )
    }

    @DisplayName("Room Entity 연결 테스트")
    @Test
    fun test2() {
        // given
        roomJpaRepository.save(
            RoomEntity(
                maxCapacity = 5,
                currCapacity = 3,
                status = RoomStatus.WAITING,
                createdAt = null,
                updatedAt = null
            )
        )

        // when
        val rooms = roomJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(rooms) },
            { assertEquals(1, rooms.size) }
        )
    }

    @DisplayName("RoomJoin Entity 연결 테스트")
    @Test
    fun test3() {
        // given
        roomJoinJpaRepository.save(
            RoomJoinEntity(
                roomId = 1L,
                playerId = 1L,
                joinedAt = LocalDateTime.now()
            )
        )

        // when
        val roomJoins = roomJoinJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(roomJoins) },
            { assertEquals(1, roomJoins.size) }
        )
    }

    @DisplayName("Game Entity 연결 테스트")
    @Test
    fun test4() {
        // given
        gameJpaRepository.save(
            GameEntity(
                roomId = 1L,
                winnerId = 1L,
                totalQuizCount = 5,
                startedAt = LocalDateTime.now(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        // when
        val games = gameJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(games) },
            { assertEquals(1, games.size) }
        )
    }

    @DisplayName("GameResult Entity 연결 테스트")
    @Test
    fun test5() {
        // given
        gameResultJpaRepository.save(
            GameResultEntity(
                gameId = 1L,
                playerId = 1L,
                totalCount = 5,
                correctCount = 5,
                ranking = 1,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        // when
        val gameResults = gameResultJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(gameResults) },
            { assertEquals(1, gameResults.size) }
        )
    }

    @DisplayName("Quiz Entity 연결 테스트")
    @Test
    fun test6() {
        // given
        quizJpaRepository.save(
            QuizEntity(
                writerId = 1L,
                question = "question",
                answer = "answer",
                category = Category.NETWORK,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        // when
        val quizzes = quizJpaRepository.findAll()

        // then
        assertAll(
            { assertNotNull(quizzes) },
            { assertEquals(1, quizzes.size) }
        )
    }

    @DisplayName("GameQuiz Entity 연결 테스트")
    @Test
    fun test7() {
        // given
        val gameQuizId = GameQuizId(1L, 1L)
        gameQuizJpaRepository.save(GameQuizEntity(id = gameQuizId))

        // when
        val gameQuiz = gameQuizJpaRepository.findByIdOrNull(GameQuizId(1L, 1L))

        // then
        assertNotNull(gameQuiz).also {
            assertEquals(1L, gameQuiz.id.gameId)
            assertEquals(1L, gameQuiz.id.quizId)
        }
    }
}
