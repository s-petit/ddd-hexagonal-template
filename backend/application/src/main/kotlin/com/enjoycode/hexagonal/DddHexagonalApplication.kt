package com.enjoycode.hexagonal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DddHexagonalApplication

fun main(args: Array<String>) {
    runApplication<DddHexagonalApplication>(*args)
}
