import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":cstar-common"))

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // test container
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:mysql")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.named<BootJar>("bootJar") {
    enabled = true
}

tasks.named<Jar>("jar") {
    enabled = true
}
