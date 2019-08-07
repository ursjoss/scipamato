plugins {
    Lib.jooqModelatorPlugin().run { id(id) version version }
}

description = "SciPaMaTo-Core :: Persistence jOOQ Project"

jooqModelator {
    jooqVersion = Lib.jooqVersion
    jooqEdition = "OSS"

    jooqConfigPath = "$rootDir/core/persistence-jooq/src/main/resources/jooqConfig.xml"
    // Important: this needs to be kept in sync with the path configured in the jooqConfig.xml
    // the reason it needs to be configured here again is for incremental build support to work
    jooqOutputPath = "build/generated-src/jooq/ch/difty/scipamato/core/db"

    migrationEngine = "FLYWAY"
    migrationsPaths = listOf("$rootDir/core/persistence-jooq/src/main/resources/db/migration/")

    dockerTag = "postgres:10"
    dockerEnv = listOf("POSTGRES_DB=scipamato", "POSTGRES_USER=scipamato", "POSTGRES_PASSWORD=scipamato")
    dockerHostPort = 15432
    dockerContainerPort = 5432
}

dependencies {
    api(project(Module.scipamatoCore("persistence-api")))
    api(project(Module.scipamatoCommon("persistence-jooq")))
    implementation(project(Module.scipamatoCore("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    jooqModelatorRuntime(Lib.postgres())
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

tasks {
    getByName("compileKotlin").dependsOn += "generateJooqMetamodel"
}