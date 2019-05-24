import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayInfoTask
import org.flywaydb.gradle.task.FlywayMigrateTask

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
        url = bootCoreProps.getProperty("spring.datasource.url")
        user = bootCoreProps.getProperty("spring.flyway.user")
        password = bootCoreProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootCoreProps.getProperty("db.schema"))
        locations = arrayOf(bootCoreProps.getProperty("spring.flyway.locations"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    val flywayMigrateIt by registering(FlywayMigrateTask::class) {
        description = "Triggers database migrations for the integration test databases"
        url = bootCoreItProps.getProperty("spring.datasource.url")
        user = bootCoreItProps.getProperty("spring.flyway.user")
        password = bootCoreItProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootCoreItProps.getProperty("db.schema"))
        locations = arrayOf(bootCoreItProps.getProperty("spring.flyway.locations"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    val flywayClean by existing(FlywayCleanTask::class) {
        description = "Drops all objects in the configured schemas of the main database"
        url = bootCoreProps.getProperty("spring.datasource.url")
        user = bootCoreProps.getProperty("spring.flyway.user")
        password = bootCoreItProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootCoreProps.getProperty("db.schema"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    register<FlywayCleanTask>("flywayCleanIt") {
        description = "Drops all objects in the configured schemas of the integration test database"
        url = bootCoreItProps.getProperty("spring.datasource.url")
        user = bootCoreItProps.getProperty("spring.flyway.user")
        password = bootCoreItProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootCoreItProps.getProperty("db.schema"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    val flywayInfo by existing(FlywayInfoTask::class) {
        description = "Prints the details and status information about all the migrations."
        url = bootCoreProps.getProperty("spring.datasource.url")
        user = bootCoreProps.getProperty("spring.flyway.user")
        password = bootCoreProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootCoreProps.getProperty("db.schema"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    register<FlywayInfoTask>("flywayInfoIt") {
        description = "Prints the details and status information about all the migrations."
        url = bootCoreItProps.getProperty("spring.datasource.url")
        user = bootCoreItProps.getProperty("spring.flyway.user")
        password = bootCoreItProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootCoreItProps.getProperty("db.schema"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    getByName("generateScipamatoCoreJooqSchemaSource").dependsOn(flywayMigrate)
    getByName("generateScipamatoCoreItJooqSchemaSource").dependsOn(flywayMigrateIt)
    getByName("compileKotlin").dependsOn += "generateScipamatoCoreJooqSchemaSource"
    getByName("compileJava").dependsOn -= "generateScipamatoCoreItJooqSchemaSource"
    getByName("integrationTest").dependsOn -= "generateScipamatoCoreItJooqSchemaSource"
}