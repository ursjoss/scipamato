@file:Suppress("SpellCheckingInspection")

import com.optravis.jooq.gradle.ContainerConfig
import com.optravis.jooq.gradle.DbConnectionConfig
import com.optravis.jooq.gradle.ExperimentalJooqGeneratorConfig
import com.optravis.jooq.gradle.GeneratorType
import com.optravis.jooq.gradle.JooqDatabaseConfig
import com.optravis.jooq.gradle.JooqGeneratorConfig
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.jooqPlugin)
    id("scipamato-integration-test")
}
description = "SciPaMaTo-Public:: Persistence jOOQ Project"

val props = file("src/integration-test/resources/application.properties").asProperties()

fun File.asProperties() = Properties().apply {
    inputStream().use { fis ->
        load(fis)
    }
}

testing {
    suites {
        @Suppress("unused")
        val integrationTest by existing {
            dependencies {
                implementation(libs.bundles.dbTest)
                runtimeOnly(libs.postgresql)
            }
        }
    }
}

@OptIn(ExperimentalJooqGeneratorConfig::class)
jooqGenerator {
    val dbUserName = props.getProperty("spring.datasource.hikari.username")
    val dbPassword = props.getProperty("spring.datasource.hikari.password")
    containerConfig = ContainerConfig(
        image = "postgres:15.4",
        port = 5432,
        environment = mapOf(
            "POSTGRES_DB" to props.getProperty("db.name"),
            "POSTGRES_USER" to dbUserName,
            "POSTGRES_PASSWORD" to dbPassword,
        ),
    )
    jooqDbConfig = JooqDatabaseConfig.postgres(recordVersionFields = listOf("version"))
    generatorConfig = JooqGeneratorConfig(
        generatorType = GeneratorType.Java,
        deprecateUnknownTypes = true,
        javaTimeTypes = false,
        daos = false,
        // pojos are required for core/core-sync only
        pojos = true,
    )
    connectionConfig = DbConnectionConfig(
        user = dbUserName,
        password = dbPassword,
        urlTemplate = "jdbc:postgresql://localhost:{{port}}/${props.getProperty("db.name")}",
    )
    packageName = "ch.difty.scipamato.publ.db"
}

dependencies {
    api(project(Module.scipamatoPublic("persistence-api")))
    api(project(Module.scipamatoCommon("persistence-jooq")))
    implementation(project(Module.scipamatoPublic("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    runtimeOnly(libs.postgresql)
    api(libs.jooq)

    testImplementation(project(Module.scipamatoCommon("persistence-jooq-test")))
    testImplementation(project(Module.scipamatoCommon("test")))
}

idea {
    module {
        inheritOutputDirs = true
    }
}
