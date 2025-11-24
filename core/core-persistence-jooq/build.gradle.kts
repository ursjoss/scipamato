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

description = "SciPaMaTo-Core :: Persistence jOOQ Project"

val props = file("src/integration-test/resources/application.properties").asProperties()

fun File.asProperties() = Properties().apply {
    inputStream().use { fis ->
        load(fis)
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
        pojos = false,
    )
    connectionConfig = DbConnectionConfig(
        user = dbUserName,
        password = dbPassword,
        urlTemplate = "jdbc:postgresql://localhost:{{port}}/${props.getProperty("db.name")}",
    )
    packageName = "ch.difty.scipamato.core.db"
}

testing {
    suites {
        @Suppress("unused")
        val integrationTest by existing {
            dependencies {
                implementation(libs.bundles.dbTest)
                runtimeOnly(libs.postgresql)
                annotationProcessor(libs.lombok)
            }
        }
    }
}

dependencies {
    api(project(Module.scipamatoCore("persistence-api")))
    api(project(Module.scipamatoCommon("persistence-jooq")))
    implementation(project(Module.scipamatoCore("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    runtimeOnly(libs.postgresql)
    api(libs.jooq)

    implementation(libs.spring.security.core)

    testImplementation(project(Module.scipamatoCommon("persistence-jooq-test")))
    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(project(Module.scipamatoCore("entity")))

    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

idea {
    module {
        inheritOutputDirs = true
    }
}
