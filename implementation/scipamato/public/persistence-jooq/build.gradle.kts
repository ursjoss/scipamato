plugins {
    Lib.jooqModelatorPlugin().run { id(id) version version }
}

description = "SciPaMaTo-Public:: Persistence jOOQ Project"

jooqModelator {
    jooqVersion = Lib.jooqVersion
    jooqEdition = "OSS"

    jooqConfigPath = "$rootDir/public/persistence-jooq/src/main/resources/jooqConfig.xml"
    // Important: this needs to be kept in sync with the path configured in the jooqConfig.xml
    // the reason it needs to be configured here again is for incremental build support to work
    jooqOutputPath = "build/generated-src/jooq/ch/difty/scipamato/publ/db"

    migrationEngine = "FLYWAY"
    migrationsPaths = listOf("$rootDir/public/persistence-jooq/src/main/resources/db/migration/")

    dockerTag = "postgres:10"
    dockerEnv = listOf("POSTGRES_DB=scipamato_public", "POSTGRES_USER=scipamatopub", "POSTGRES_PASSWORD=scipamatopub")
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

    testCompile(project(Module.scipamatoCommon("persistence-jooq-test")))
    testCompile(project(Module.scipamatoCommon("test")))

    testCompile(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

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

tasks {
    getByName("compileKotlin").dependsOn += "generateJooqMetamodel"
}