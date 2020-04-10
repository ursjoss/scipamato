import ch.ayedo.jooqmodelator.gradle.JooqModelatorTask

plugins {
    Lib.jooqModelatorPlugin().run { id(id) version version }
}

description = "SciPaMaTo-Public:: Persistence jOOQ Project"

val props = file("src/integration-test/resources/application.properties").asProperties()

jooqModelator {
    jooqVersion = Lib.jooqVersion
    jooqEdition = "OSS"

    jooqConfigPath = "$rootDir/public/public-persistence-jooq/src/main/resources/jooqConfig.xml"
    // Important: this needs to be kept in sync with the path configured in the jooqConfig.xml
    // the reason it needs to be configured here again is for incremental build support to work
    jooqOutputPath = "build/generated-src/jooq/ch/difty/scipamato/publ/db"

    migrationEngine = "FLYWAY"
    migrationsPaths = listOf("$rootDir/public/public-persistence-jooq/src/main/resources/db/migration/")

    dockerTag = "postgres:10"

    dockerEnv = listOf(
        "POSTGRES_DB=${props.getProperty("db.name")}",
        "POSTGRES_USER=${props.getProperty("spring.datasource.hikari.username")}",
        "POSTGRES_PASSWORD=${props.getProperty("spring.datasource.hikari.password")}"
    )
    dockerHostPort = 15432
    dockerContainerPort = 5432
}

dependencies {
    api(project(Module.scipamatoPublic("persistence-api")))
    api(project(Module.scipamatoCommon("persistence-jooq")))
    implementation(project(Module.scipamatoPublic("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    jooqModelatorRuntime(Lib.postgres())
    runtimeOnly(Lib.postgres())
    api(Lib.jOOQ("jooq"))

    implementation(Lib.commonsLang3())
    implementation(Lib.commonsCollection())

    testImplementation(project(Module.scipamatoCommon("persistence-jooq-test")))
    testImplementation(project(Module.scipamatoCommon("test")))

    testImplementation(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

    integrationTestImplementation(Lib.testcontainers("testcontainers"))
    integrationTestImplementation(Lib.testcontainers("junit-jupiter"))
    integrationTestImplementation(Lib.testcontainers("postgresql"))
    integrationTestRuntimeOnly(Lib.postgres())
    integrationTestAnnotationProcessor(Lib.lombok())
}

val generatedSourcesPath = "build/generated-src/jooq"
sourceSets {
    main {
        java {
            srcDir(setOf(generatedSourcesPath, "src/main/java"))
        }
    }
}

tasks {
    val jooqMetamodelTaskName = "generateJooqMetamodel"
    withType<JooqModelatorTask> {
        // prevent parallel run of this task between core and public
        outputs.dir("${rootProject.buildDir}/$jooqMetamodelTaskName")
    }
    getByName("compileKotlin").dependsOn += jooqMetamodelTaskName
}

idea {
    module {
        inheritOutputDirs = true
    }
}
