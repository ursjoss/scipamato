import org.flywaydb.gradle.task.AbstractFlywayTask
import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayInfoTask
import org.flywaydb.gradle.task.FlywayMigrateTask
import java.util.*

plugins {
    Lib.jooqPlugin().run { id(id) version version }
    Lib.flywayPlugin().run { id(id) version version }
}

description = "SciPaMaTo-Core :: Persistence jOOQ Project"

configurations {
    create("flywayMigration")
}

dependencies {
    api(project(Module.scipamatoCore("persistence-api")))
    api(project(Module.scipamatoCommon("persistence-jooq")))
    implementation(project(Module.scipamatoCore("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    jooqRuntime(Lib.postgres())
    runtimeOnly(Lib.postgres())
    api(Lib.jOOQ("jooq"))

    implementation(Lib.springSecurity("core"))
    implementation(Lib.commonsLang3())
    implementation(Lib.commonsCollection())

    testCompile(project(Module.scipamatoCommon("persistence-jooq-test")))
    testCompile(project(Module.scipamatoCommon("test")))
    testCompile(project(Module.scipamatoCore("entity")))

    testCompile(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

    flywayMigration(Lib.postgres())
    integrationTestRuntimeOnly(Lib.postgres())
}

val bootCoreProps = file("src/main/resources/application.properties").asProperties()
val bootCoreItProps = file("src/intTest/resources/application.properties").asProperties()

val generatedSourcesPath = "build/generated-src/jooq"
sourceSets {
    main {
        java {
            srcDir(setOf(generatedSourcesPath, "src/main/java"))
        }
    }
}

apply(from = "$rootDir/gradle/jooq-core.gradle")

tasks {
    val flywayMigrate by existing(FlywayMigrateTask::class) {
        description = "Triggers database migrations for the main database"
        configureApplying(bootCoreProps)
    }
    val flywayMigrateIt by registering(FlywayMigrateTask::class) {
        description = "Triggers database migrations for the integration test databases"
        configureApplying(bootCoreItProps)
    }
    flywayClean {
        description = "Drops all objects in the configured schemas of the main database"
        configureApplying(bootCoreProps)
    }
    register<FlywayCleanTask>("flywayCleanIt") {
        description = "Drops all objects in the configured schemas of the integration test database"
        configureApplying(bootCoreItProps)
    }
    flywayInfo {
        description = "Prints the details and status information about all the migrations."
        configureApplying(bootCoreProps)
    }
    register<FlywayInfoTask>("flywayInfoIt") {
        description = "Prints the details and status information about all the migrations."
        configureApplying(bootCoreItProps)
    }

    named("generateScipamatoCoreJooqSchemaSource") {
        dependsOn(flywayMigrate)
    }
    getByName("generateScipamatoCoreItJooqSchemaSource").dependsOn(flywayMigrateIt)
    getByName("compileKotlin").dependsOn += "generateScipamatoCoreJooqSchemaSource"
    getByName("compileJava").dependsOn -= "generateScipamatoCoreItJooqSchemaSource"
    getByName("integrationTest").dependsOn -= "generateScipamatoCoreItJooqSchemaSource"
}

fun AbstractFlywayTask.configureApplying(props: Properties) {
    url = props.getProperty("spring.datasource.url")
    user = props.getProperty("spring.flyway.user")
    password = props.getProperty("spring.flyway.password")
    schemas = arrayOf(props.getProperty("db.schema"))
    locations = arrayOf(props.getProperty("spring.flyway.locations"))
    configurations = arrayOf("compile", "flywayMigration")
}