package yjh.cstar

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

/**
 * 통합 테스트를 위한 REDIS 싱글톤 컨테이너 입니다.
 * 테스트 전역에서 싱글톤으로 접근해서 사용합니다.
 * 최초 테스트 실행시 JVM static 영역에 로드됩니다.
 */
object RedisTestContainer {
    val REDIS_CONTAINER: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:latest"))
        .withExposedPorts(6379)

    init {
        REDIS_CONTAINER.start()
    }
}
