package ch.difty.scipamato.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
open class TestApplication {

    @Suppress("SpreadOperator")
    open fun main(args: Array<String>) {
        SpringApplicationBuilder()
            .sources(TestApplication::class.java)
            .run(*args)
    }
}
