package yjh.cstar.game.application

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.game.infrastructure.redis.AnswerResultEntity
import yjh.cstar.game.infrastructure.redis.RedisQueueRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("[Application 테스트] GameAnswerQueueService")
class GameAnswerQueueServiceTest {

    @Autowired
    private lateinit var redisQueueRepository: RedisQueueRepository

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var gameAnswerQueueService: GameAnswerQueueService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        private const val ROOM_ID = 1L
        private const val QUIZ_ID = 1L
        private const val KEY = "roomId : " + ROOM_ID + ", " + "quizId : " + QUIZ_ID

        private val redis: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379)
            .withReuse(true)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", redis::getHost)
            registry.add("spring.data.redis.port", redis::getFirstMappedPort)
        }

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            redis.start()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            redis.stop()
        }
    }

    @BeforeTest
    fun beforeEach() {
        redisQueueRepository.deleteAll(KEY)
    }

    @AfterTest
    fun afterEach() {
        redisQueueRepository.deleteAll(KEY)
    }

    @Test
    fun `플레이어 정답 선착순 제출 테스트`() {
        // given
        val answerResult = AnswerResult(
            answer = "answer",
            roomId = ROOM_ID,
            quizId = QUIZ_ID,
            playerId = 1
        )

        // when
        assertDoesNotThrow { gameAnswerQueueService.add(answerResult) }

        // then
        val size = redisTemplate.opsForList().size(KEY)
        assertEquals(1, size)
    }

    @Test
    fun `플레이어 정답 선착순 뽑아오기 테스트`() {
        // given
        val answerResultEntity = AnswerResultEntity(
            answer = "answer",
            roomId = ROOM_ID,
            quizId = QUIZ_ID,
            playerId = 1
        )
        val value = objectMapper.writeValueAsString(answerResultEntity)
        redisTemplate.opsForList().rightPush(KEY, value)

//        val aaa = redisTemplate.opsForList().size(KEY)
//        assertEquals(1, aaa)

        // when
        val answerResult = gameAnswerQueueService.poll(ROOM_ID, QUIZ_ID)

        // then
        assertNotNull(answerResult)
        assertEquals("answer", answerResult.answer)
        assertEquals(ROOM_ID, answerResult.roomId)
        assertEquals(QUIZ_ID, answerResult.quizId)
        assertEquals(1, answerResult.playerId)

        val size = redisTemplate.opsForList().size(KEY)
        assertEquals(0, size)
    }
}
