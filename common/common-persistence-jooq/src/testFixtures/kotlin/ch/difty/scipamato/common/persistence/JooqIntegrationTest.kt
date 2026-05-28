package ch.difty.scipamato.common.persistence

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration
import org.springframework.boot.jooq.test.autoconfigure.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@JooqTest
@Testcontainers
@ImportAutoConfiguration(FlywayAutoConfiguration::class)
@Suppress("unused")
annotation class JooqIntegrationTest
