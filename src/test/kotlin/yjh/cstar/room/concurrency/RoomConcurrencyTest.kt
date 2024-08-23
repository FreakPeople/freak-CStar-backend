package yjh.cstar.room.concurrency

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import yjh.cstar.member.infrastructure.jpa.MemberEntity
import yjh.cstar.member.infrastructure.jpa.MemberJpaRepository
import yjh.cstar.room.domain.RoomStatus
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository
import yjh.cstar.room.presentation.RoomController
import yjh.cstar.room.presentation.request.RoomJoinRequest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("[동시성 테스트] RoomController")
@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomConcurrencyTest {

    @Autowired
    private lateinit var roomController: RoomController

    @Autowired
    private lateinit var roomJpaRepository: RoomJpaRepository

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @BeforeTest
    fun clearBefore() {
        roomJpaRepository.deleteAll()
        memberJpaRepository.deleteAll()
    }

    @AfterTest
    fun clearAfter() {
        roomJpaRepository.deleteAll()
        memberJpaRepository.deleteAll()
    }

    @Test
    fun `게임 방 참가 요청시 참가 인원 수 동시성 테스트`() {
        // given
        val roomId = roomJpaRepository.save(
            RoomEntity(
                maxCapacity = 100,
                currCapacity = 0,
                status = RoomStatus.WAITING,
                createdAt = null,
                updatedAt = null,
            )
        ).toModel().id

        val memberId = memberJpaRepository.save(
            MemberEntity(
                email = "test@test.com",
                password = "12345",
                nickname = "testNickname",
                createdAt = null,
                updatedAt = null,
            )
        ).toModel().id

        val numberOfThreads = 50
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(numberOfThreads)
        val executor = Executors.newFixedThreadPool(numberOfThreads)


        // when
        for (idx in 1..numberOfThreads) {
            executor.submit {
                try {
                    roomController.join(roomId, RoomJoinRequest(memberId))
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
        val room = roomJpaRepository.findByIdOrNull(roomId)?.toModel()
        assertNotNull(room)
        assertEquals(50, room.currCapacity) // 최대 5명만 수용 가능해야함
    }
}

