package yjh.cstar

import org.testcontainers.containers.MySQLContainer

/**
 * 통합 테스트를 위한 MYSQL 싱글톤 컨테이너 입니다.
 * 테스트 전역에서 싱글톤으로 접근해서 사용합니다.
 * 최초 테스트 실행시 JVM static 영역에 로드됩니다.
 */
object MySQLTestContainer {
    val MYSQL_CONTAINER = MySQLContainer("mysql:8.0.35").apply {
        withDatabaseName("test-db")
        withUsername("root")
        withPassword("1234")
        withUrlParam("serverTimeZone", "UTC")
        withUrlParam("useSSL", "false")
        withUrlParam("allowPublicKeyRetrieval", "true")
    }

    init {
        MYSQL_CONTAINER.start()
    }
}

// Example - Java 싱글톤 클래스
//
// public class MySQLTestContainer {
//
//     private static final MySQLContainer<?> MYSQL_CONTAINER;
//
//     static {
//         MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0.35")
//             .withDatabaseName("test-db")
//             .withUsername("root")
//             .withPassword("1234")
//             .withUrlParam("serverTimeZone", "UTC")
//             .withUrlParam("useSSL", "false")
//             .withUrlParam("allowPublicKeyRetrieval", "true");
//
//         MYSQL_CONTAINER.start();
//     }
//
//     private MySQLTestContainer() {
//         // Private constructor to prevent instantiation
//     }
//
//     public static MySQLContainer<?> getInstance() {
//         return MYSQL_CONTAINER;
//     }
// }
