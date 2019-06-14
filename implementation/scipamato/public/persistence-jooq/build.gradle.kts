import org.flywaydb.gradle.task.AbstractFlywayTask
import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayInfoTask
import org.flywaydb.gradle.task.FlywayMigrateTask
import java.util.*

plugins {
    Lib.jooqPlugin().run { id(id) version version }
    Lib.flywayPlugin().run { id(id) version version }
}

description = "SciPaMaTo-Public:: Persistence jOOQ Project"

configurations {
    create("flywayMigration")
}

dependencies {
    api(project(Module.scipamatoPublic("persistence-api")))
    api(project(Module.scipamatoCommon("persistence-jooq")))
    implementation(project(Module.scipamatoPublic("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    jooqRuntime(Lib.postgres())
    runtimeOnly(Lib.postgres())
    api(Lib.jOOQ("jooq"))

    implementation(Lib.commonsLang3())
    implementation(Lib.commonsCollection())

    testCompile(project(Module.scipamatoCommon("persistence-jooq-test")))
    testCompile(project(Module.scipamatoCommon("test")))

    testCompile(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

    flywayMigration(Lib.postgres())

    integrationTestRuntimeOnly(Lib.postgres())
    integrationTestAnnotationProcessor(Lib.lombok())
}

val bootPubProps = file("src/main/resources/application.properties").asProperties()
val bootPubItProps = file("src/intTest/resources/application.properties").asProperties()

val generatedSourcesPath = "build/generated-src/jooq"
sourceSets {
    main {
        java {
            srcDir(setOf(generatedSourcesPath, "src/main/java"))
        }
    }
}

apply(from = "$rootDir/gradle/jooq-public.gradle")

tasks {
    val flywayMigrate by existing(FlywayMigrateTask::class) {
        description = "Triggers database migrations for the main database"
        configureApplying(bootPubProps)
    }

    val flywayMigrateIt by registering(FlywayMigrateTask::class) {
        description = "Triggers database migrations for the integration test databases"
        configureApplying(bootPubItProps)
    }

    flywayClean {
        description = "Drops all objects in the configured schemas of the main database"
        configureApplying(bootPubProps)
    }
    register<FlywayCleanTask>("flywayCleanIt") {
        description = "Drops all objects in the configured schemas of the integration test database"
        configureApplying(bootPubItProps)
    }

    flywayInfo {
        description = "Prints the details and status information about all the migrations."
        configureApplying(bootPubProps)
    }
    register<FlywayInfoTask>("flywayInfoIt") {
        description = "Prints the details and status information about all the migrations."
        configureApplying(bootPubItProps)
    }

    getByName("generateScipamatoPublicJooqSchemaSource").dependsOn(flywayMigrate)
    getByName("generateScipamatoPublicItJooqSchemaSource").dependsOn(flywayMigrateIt)
    getByName("compileKotlin").dependsOn += "generateScipamatoPublicJooqSchemaSource"
    getByName("compileJava").dependsOn -= "generateScipamatoPublicItJooqSchemaSource"
    getByName("integrationTest").dependsOn -= "generateScipamatoPublicItJooqSchemaSource"
}

fun AbstractFlywayTask.configureApplying(props: Properties) {
    url = props.getProperty("spring.datasource.url")
    user = props.getProperty("spring.flyway.user")
    password = props.getProperty("spring.flyway.password")
    schemas = arrayOf(props.getProperty("db.schema"))
    locations = arrayOf(props.getProperty("spring.flyway.locations"))
    configurations = arrayOf("compile", "flywayMigration")
}