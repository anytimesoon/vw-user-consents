package xyz.delartigue.vw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VwApplication

fun main(args: Array<String>) {
    runApplication<VwApplication>(*args)
}
