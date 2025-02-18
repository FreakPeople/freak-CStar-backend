import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":cstar-common"))

    // flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // test container
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:mysql")

    // 나중에 커먼으로
    // jwt
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.4")
}

tasks.named<BootJar>("bootJar") {
    enabled = true
}

tasks.named<Jar>("jar") {
    enabled = true
}
