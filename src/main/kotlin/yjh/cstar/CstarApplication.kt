package yjh.cstar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class CstarApplication

fun main(args: Array<String>) {
    runApplication<CstarApplication>(*args)
}
