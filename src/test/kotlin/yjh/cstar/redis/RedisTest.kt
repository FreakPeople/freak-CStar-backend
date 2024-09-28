package yjh.cstar.redis

import com.fasterxml.jackson.databind.ObjectMapper
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
import yjh.cstar.chat.infrastructure.RedisAnswerMessageBroker
import yjh.cstar.play.infrastructure.redis.PlayerAnswerEntity
import yjh.cstar.util.RedisUtil
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("[Redis 테스트] Redis")
class RedisTest {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var redisUtil: RedisUtil

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        private const val ROOM_ID = 1L
        private const val QUIZ_ID = 1L
        private val KEY = RedisAnswerMessageBroker.getKey(ROOM_ID, QUIZ_ID)

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
    fun `레디스 큐에 데이터 삽입 테스트`() {
        // given
        val value_1 = objectMapper.writeValueAsString(PlayerAnswerEntity("ans_1", QUIZ_ID, ROOM_ID, 1, "nickname"))
        redisUtil.rpush(KEY, value_1)
        val value_2 = objectMapper.writeValueAsString(PlayerAnswerEntity("ans_2", QUIZ_ID, ROOM_ID, 1, "nickname"))
        redisUtil.rpush(KEY, value_2)

        // when
        val size = redisUtil.size(KEY)

        // then
        assertNotNull(size)
        assertEquals(2, size)
    }

    @Test
    fun `레디스 큐에 데이터 반환 테스트`() {
        // given
        val value_1 = objectMapper.writeValueAsString(PlayerAnswerEntity("ans_1", QUIZ_ID, ROOM_ID, 1, "nickname"))
        redisUtil.rpush(KEY, value_1)
        val value_2 = objectMapper.writeValueAsString(PlayerAnswerEntity("ans_2", QUIZ_ID, ROOM_ID, 1, "nickname"))
        redisUtil.rpush(KEY, value_2)

        // when
        val first = objectMapper.readValue(redisUtil.lpop(KEY), PlayerAnswerEntity::class.java)
        val second = objectMapper.readValue(redisUtil.lpop(KEY), PlayerAnswerEntity::class.java)
        val size = redisUtil.size(KEY)

        // then
        assertNotNull(first)
        assertNotNull(second)
        assertEquals(0, size)
        assertEquals("ans_1", first.answer)
        assertEquals("ans_2", second.answer)
    }

    @Test
    fun `레디스 큐의 특정 key에 해당하는 큐 삭제 테스트`() {
        // given
        val value_1 = objectMapper.writeValueAsString(PlayerAnswerEntity("ans_1", QUIZ_ID, ROOM_ID, 1, "nickname"))
        redisUtil.rpush(KEY, value_1)
        val value_2 = objectMapper.writeValueAsString(PlayerAnswerEntity("ans_2", QUIZ_ID, ROOM_ID, 1, "nickname"))
        redisUtil.rpush(KEY, value_2)

        // when
        redisUtil.delete(KEY)

        // then
        val size = redisTemplate.opsForList().size(KEY)
        assertEquals(0, size)

        val hasKey = redisTemplate.hasKey(KEY)
        assertFalse(hasKey)
    }

    @Test
    fun `레디스 Blocking Queue 동작 테스트`() {
        // given
        val value = objectMapper.writeValueAsString(PlayerAnswerEntity("ans_1", QUIZ_ID, ROOM_ID, 1, "nickname"))
        repeat(5) {
            redisUtil.rpush(KEY, value)
        }
        val result = mutableListOf<String>()

        // when
        generateSequence { redisUtil.lpop(KEY, 5) }
            .forEach { result.add(it) }

        // then
        assertEquals(5, result.size)
    }
}
