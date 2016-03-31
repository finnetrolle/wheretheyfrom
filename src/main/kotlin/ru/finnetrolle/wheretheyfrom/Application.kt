package ru.finnetrolle.wheretheyfrom

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin on 31.03.16.
 */

@SpringBootApplication
open class Application


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}