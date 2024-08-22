package yjh.cstar.member.concurrency

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import yjh.cstar.member.application.MemberService
import yjh.cstar.member.domain.MemberCreateCommand
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

/**
 * 주의) 별도의 스레드를 통해서 테스트 동작을 수행하기 때문에,
 * @Transactional 를 사용하지 않아야 함(사용하면 올바르게 동작하지 않음)
 */
@DisplayName("[동시성 테스트] MemberService")
@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberConcurrencyTest {

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @BeforeTest
    fun clearBefore() {
        memberJpaRepository.deleteAll()
    }

    @AfterTest
    fun clearAfter() {
        memberJpaRepository.deleteAll()
    }

    @Test
    fun `회원 생성시 email 중복 동시성 테스트`() {
        // given
        val numberOfThreads = 10
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(numberOfThreads)
        val executor = Executors.newFixedThreadPool(numberOfThreads)

        // when
        for (idx in 1..numberOfThreads) {
            executor.submit {
                try {
                    memberService.create(
                        MemberCreateCommand(
                            email = "email@naver.com",
                            password = "password",
                            nickname = "nickname" + idx
                        )
                    )
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
        val members = memberJpaRepository.findAll()
        assertEquals(1, members.size) // 1개만 생성 되었어야 함.
    }

    @Test
    fun `회원 생성시 nickname 중복 동시성 테스트`() {
        // given
        val numberOfThreads = 10
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(numberOfThreads)
        val executor = Executors.newFixedThreadPool(numberOfThreads)

        // when
        for (idx in 1..numberOfThreads) {
            executor.submit {
                try {
                    memberService.create(
                        MemberCreateCommand(
                            email = "email@naver.com" + idx,
                            password = "password",
                            nickname = "nickname"
                        )
                    )
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                } finally {
                    doneLatch.countDown()
                }
            }
        }

        startLatch.countDown()
        doneLatch.await()

        executor.shutdown()

        // then
        val members = memberJpaRepository.findAll()
        assertEquals(1, members.size)
    }
}
