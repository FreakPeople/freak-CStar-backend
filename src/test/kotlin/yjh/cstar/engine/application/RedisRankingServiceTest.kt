package yjh.cstar.engine.application

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("[Application 테스트] RedisRankingService")
class RedisRankingServiceTest {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var redisRankingService: RedisRankingService

    companion object {

        private val ROOM_ID = 1L
        private val KEY = RedisRankingService.KEY_PREFIX + ROOM_ID

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
        redisTemplate.delete(KEY)
    }

    @AfterTest
    fun afterEach() {
        redisTemplate.delete(KEY)
    }

    @Test
    fun `회원 랭킹 초기화 테스트`() {
        // given
        val players = listOf(1L, 2L, 3L, 4L, 5L)

        // when
        redisRankingService.init(ROOM_ID, players)

        // then
        assertTrue(redisTemplate.hasKey(KEY))
        assertEquals(5, redisTemplate.opsForZSet().size(KEY))

        assertEquals(0.toDouble(), redisTemplate.opsForZSet().score(KEY, RedisRankingService.VALUE_PREFIX + 1L))
        assertEquals(0.toDouble(), redisTemplate.opsForZSet().score(KEY, RedisRankingService.VALUE_PREFIX + 2L))
        assertEquals(0.toDouble(), redisTemplate.opsForZSet().score(KEY, RedisRankingService.VALUE_PREFIX + 3L))
        assertEquals(0.toDouble(), redisTemplate.opsForZSet().score(KEY, RedisRankingService.VALUE_PREFIX + 4L))
        assertEquals(0.toDouble(), redisTemplate.opsForZSet().score(KEY, RedisRankingService.VALUE_PREFIX + 5L))
    }

    @Test
    fun `스코어 증가 테스트`() {
        // given
        val players = listOf(1L)

        // then
        redisRankingService.increaseScore(ROOM_ID, 1L)

        // then
        assertEquals(1.toDouble(), redisTemplate.opsForZSet().score(KEY, RedisRankingService.VALUE_PREFIX + 1L))
    }

    @Test
    fun `랭킹 가져오기 테스트`() {
        // given
        val value1 = RedisRankingService.VALUE_PREFIX + 1L
        val value2 = RedisRankingService.VALUE_PREFIX + 2L
        val value3 = RedisRankingService.VALUE_PREFIX + 3L
        redisTemplate.opsForZSet().incrementScore(KEY, value1, 1.toDouble())
        redisTemplate.opsForZSet().incrementScore(KEY, value3, 3.toDouble())
        redisTemplate.opsForZSet().incrementScore(KEY, value2, 2.toDouble())

        // then
        val result = redisRankingService.getRanking(ROOM_ID)

        // then
        assertEquals(3, result.size)
        assertEquals(3.toDouble(), result.get(0).second)
        assertEquals(2.toDouble(), result.get(1).second)
        assertEquals(1.toDouble(), result.get(2).second)
    }

    @Test
    fun `최상위 랭킹 승자 가져오기 테스트 1`() {
        // given
        val value1 = RedisRankingService.VALUE_PREFIX + 1L
        val value2 = RedisRankingService.VALUE_PREFIX + 2L
        val value3 = RedisRankingService.VALUE_PREFIX + 3L
        redisTemplate.opsForZSet().incrementScore(KEY, value1, 1.toDouble())
        redisTemplate.opsForZSet().incrementScore(KEY, value3, 3.toDouble())
        redisTemplate.opsForZSet().incrementScore(KEY, value2, 2.toDouble())

        // then
        val result = redisRankingService.getWinnerId(ROOM_ID)

        // then
        assertNotNull(result)
        assertEquals(3L, result)
    }

    @Test
    fun `최상위 랭킹 승자 가져오기 테스트 2`() {
        // given
        val value1 = RedisRankingService.VALUE_PREFIX + 1L
        val value2 = RedisRankingService.VALUE_PREFIX + 2L
        val value3 = RedisRankingService.VALUE_PREFIX + 333L
        redisTemplate.opsForZSet().incrementScore(KEY, value1, 1.toDouble())
        redisTemplate.opsForZSet().incrementScore(KEY, value3, 100.toDouble())
        redisTemplate.opsForZSet().incrementScore(KEY, value2, 2.toDouble())

        // then
        val result = redisRankingService.getWinnerId(ROOM_ID)

        // then
        assertNotNull(result)
        assertEquals(333L, result)
    }

    @Test
    fun `최상위 랭킹 승자 가져올 때 플레이어가 없는경우 -1 반환`() {
        // then
        val result = redisRankingService.getWinnerId(ROOM_ID)

        // then
        assertNotNull(result)
        assertEquals(-1L, result)
    }
}
