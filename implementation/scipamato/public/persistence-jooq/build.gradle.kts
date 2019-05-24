import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayInfoTask
import org.flywaydb.gradle.task.FlywayMigrateTask

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
        url = bootPubProps.getProperty("spring.datasource.url")
        user = bootPubProps.getProperty("spring.flyway.user")
        password = bootPubProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootPubProps.getProperty("db.schema"))
        locations = arrayOf(bootPubProps.getProperty("spring.flyway.locations"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    val flywayMigrateIt by registering(FlywayMigrateTask::class) {
        description = "Triggers database migrations for the integration test databases"
        url = bootPubItProps.getProperty("spring.datasource.url")
        user = bootPubItProps.getProperty("spring.flyway.user")
        password = bootPubItProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootPubItProps.getProperty("db.schema"))
        locations = arrayOf(bootPubItProps.getProperty("spring.flyway.locations"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    val flywayClean by existing(FlywayCleanTask::class) {
        description = "Drops all objects in the configured schemas of the main database"
        url = bootPubProps.getProperty("spring.datasource.url")
        user = bootPubProps.getProperty("spring.flyway.user")
        password = bootPubItProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootPubProps.getProperty("db.schema"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    register<FlywayCleanTask>("flywayCleanIt") {
        description = "Drops all objects in the configured schemas of the integration test database"
        url = bootPubItProps.getProperty("spring.datasource.url")
        user = bootPubItProps.getProperty("spring.flyway.user")
        password = bootPubItProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootPubItProps.getProperty("db.schema"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    val flywayInfo by existing(FlywayInfoTask::class) {
        description = "Prints the details and status information about all the migrations."
        url = bootPubProps.getProperty("spring.datasource.url")
        user = bootPubProps.getProperty("spring.flyway.user")
        password = bootPubProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootPubProps.getProperty("db.schema"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    register<FlywayInfoTask>("flywayInfoIt") {
        description = "Prints the details and status information about all the migrations."
        url = bootPubItProps.getProperty("spring.datasource.url")
        user = bootPubItProps.getProperty("spring.flyway.user")
        password = bootPubItProps.getProperty("spring.flyway.password")
        schemas = arrayOf(bootPubItProps.getProperty("db.schema"))
        configurations = arrayOf("compile", "flywayMigration")
    }

    getByName("generateScipamatoPublicJooqSchemaSource").dependsOn(flywayMigrate)
    getByName("generateScipamatoPublicItJooqSchemaSource").dependsOn(flywayMigrateIt)
    getByName("compileKotlin").dependsOn += "generateScipamatoPublicJooqSchemaSource"
    getByName("compileJava").dependsOn -= "generateScipamatoPublicItJooqSchemaSource"
    getByName("integrationTest").dependsOn -= "generateScipamatoPublicItJooqSchemaSource"
}