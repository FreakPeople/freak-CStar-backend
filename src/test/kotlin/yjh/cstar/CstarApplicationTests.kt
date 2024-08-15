package yjh.cstar

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import kotlin.test.Test

@ActiveProfiles("local-test") // 어떤 applicaion.yml 파일을 사용할지 선택
@Sql(value = ["file:src/main/resources/db/init_schema.sql"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CstarApplicationTests : IntegrationTest() {

    @Test
    fun contextLoads() {
    }
}
