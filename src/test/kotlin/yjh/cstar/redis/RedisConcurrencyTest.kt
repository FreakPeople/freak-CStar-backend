package yjh.cstar.redis

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import yjh.cstar.engine.application.port.GameAnswerPollRepository
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.util.RedisUtil
import yjh.cstar.websocket.application.GameAnswerPushService
import java.util.Collections
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("[Redis 동시성 테스트] GameAnswerPollRepository")
class RedisConcurrencyTest {

    @Autowired
    private lateinit var gameAnswerPushService: GameAnswerPushService

    @Autowired
    private lateinit var gameAnswerPollRepository: GameAnswerPollRepository

    @Autowired
    private lateinit var redisUtil: RedisUtil

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
        redisUtil.delete(KEY)
    }

    @AfterTest
    fun afterEach() {
        redisUtil.delete(KEY)
    }

    @Test
    fun `레디스 사용자 퀴즈 정답 큐 동시성 테스트`() {
        // given
        val numberOfThreads = 2
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(numberOfThreads)
        val executor = Executors.newFixedThreadPool(numberOfThreads)
        val receive = Collections.synchronizedList(mutableListOf<AnswerResult>())

        val answerResult = AnswerResult(
            answer = "정답",
            quizId = QUIZ_ID,
            roomId = ROOM_ID,
            playerId = 1,
            nickname = "nickname"
        )

        // when
        executor.submit {
            try {
                for (idx in 1..100) {
                    gameAnswerPushService.push(answerResult)
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                doneLatch.countDown()
            }
        }

        executor.submit {
            try {
                for (idx in 1..100) {
                    receive.add(gameAnswerPollRepository.poll(ROOM_ID, QUIZ_ID))
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                doneLatch.countDown()
            }
        }

        startLatch.countDown() // 모든 스레드 동시에 시작

        doneLatch.await() // 모든 스레드 종료 대기

        executor.shutdown()

        // then
        val pollSize = receive.filter { it != null }
            .size
        val remainPushSize = redisUtil.size(KEY) ?: 0

        assertEquals(100, pollSize + remainPushSize)
    }

    @Test
    fun `레디스 List 삽입 동시성 테스트`() {
        // given
        val numberOfThreads = 100
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(numberOfThreads)
        val executor = Executors.newFixedThreadPool(numberOfThreads)

        val answerResult = AnswerResult(
            answer = "정답",
            quizId = QUIZ_ID,
            roomId = ROOM_ID,
            playerId = 1,
            nickname = "nickname"
        )

        // when
        // 데이터 추가
        for (idx in 1..numberOfThreads) {
            executor.submit {
                try {
                    gameAnswerPushService.push(answerResult)
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                } finally {
                    doneLatch.countDown()
                }
            }
        }

        startLatch.countDown() // 모든 스레드 동시에 시작

        doneLatch.await() // 모든 스레드 종료 대기

        executor.shutdown()

        // then
        val remainPushSize = redisUtil.size(KEY) ?: 0

        assertEquals(100, remainPushSize)
    }
}
