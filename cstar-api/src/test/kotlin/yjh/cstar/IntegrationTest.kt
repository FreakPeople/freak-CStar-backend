package yjh.cstar

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import yjh.cstar.MySQLTestContainer.MYSQL_CONTAINER
import yjh.cstar.RedisTestContainer.REDIS_CONTAINER

/**
 * 통합 테스트를 사용할 때 다음 추상클래스를 상속해서 사용합니다.
 * 테스트 시작시 프로젝트에 사용되는 infra 환경(ex mysql, redis)은 싱글톤 기반의 TestContainer 로 구동중입니다.
 */
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class IntegrationTest {

    /**
     * SpringBoot .java 파일에서 kotlin companion object 에 접근하여 java 의 static 처럼 사용하기 위해 @JvmStatic 을 사용합니다.
     * Application Context 로드시 동적으로 컨테이너 정보를 테스트용 application.yml 에 주입하기 위해 @DynamicPropertySource 를 사용합니다.
     */
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun setDataSourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl)
            registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername)
            registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword)
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost)
            registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort)
        }
    }
}
