import ch.ayedo.jooqmodelator.gradle.JooqModelatorTask

plugins {
    Lib.jooqModelatorPlugin().run { id(id) version version }
}

description = "SciPaMaTo-Core :: Persistence jOOQ Project"

val jooqConfigFile = "$rootDir/core/core-persistence-jooq/src/main/resources/jooqConfig.xml"
val props = file("src/integration-test/resources/application.properties").asProperties()

jooqModelator {
    jooqVersion = Lib.jooqVersion
    jooqEdition = "OSS"

    jooqConfigPath = jooqConfigFile
    // Important: this needs to be kept in sync with the path configured in the jooqConfig.xml
    // the reason it needs to be configured here again is for incremental build support to work
    jooqOutputPath = "build/generated-src/jooq/ch/difty/scipamato/core/db"

    migrationEngine = "FLYWAY"
    migrationsPaths = listOf("$rootDir/core/core-persistence-jooq/src/main/resources/db/migration/")

    dockerTag = "postgres:10"
    dockerEnv = listOf(
        "POSTGRES_DB=scipamato",
        "POSTGRES_USER=scipamato",
        "POSTGRES_PASSWORD=scipamato"
    )
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

    testImplementation(project(Module.scipamatoCommon("persistence-jooq-test")))
    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(project(Module.scipamatoCore("entity")))

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
    val processResources by existing {
        val resourcesDir = sourceSets.main.get().output.resourcesDir
        resourcesDir?.mkdirs()
        val fileContent = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <configuration>
                    <jdbc>
                        <driver>org.postgresql.Driver</driver>
                        <url>jdbc:postgresql://localhost:15432/scipamato?loggerLevel=OFF</url>
                        <user>scipamato</user>
                        <password>scipamato</password>
                    </jdbc>
                    <generator>
                        <name>org.jooq.codegen.DefaultGenerator</name>
                        <database>
                            <name>org.jooq.meta.postgres.PostgresDatabase</name>
                            <inputSchema>public</inputSchema>
                            <recordVersionFields>version</recordVersionFields>
                        </database>
                        <generate>
                            <deprecated>false</deprecated>
                            <instanceFields>true</instanceFields>
                            <pojos>false</pojos>
                            <javaTimeTypes>false</javaTimeTypes>
                            <springAnnotations>true</springAnnotations>
                        </generate>
                        <!-- Important: Keep in sync with jooqOutputPath build.gradle -->
                        <target>
                            <packageName>ch.difty.scipamato.core.db</packageName>
                            <directory>core/core-persistence-jooq/build/generated-src/jooq</directory>
                        </target>
                    </generator>
                    <basedir>$rootDir</basedir>
                </configuration>
            """.trimIndent()
        File(jooqConfigFile).writeText(fileContent)

    }
    val jooqMetamodelTaskName = "generateJooqMetamodel"
    withType<JooqModelatorTask> {
        // prevent parallel run of this task between core and public
        outputs.dir("${rootProject.buildDir}/$jooqMetamodelTaskName")
        dependsOn(processResources)
    }
    getByName("compileKotlin").dependsOn += jooqMetamodelTaskName
}

idea {
    module {
        inheritOutputDirs = true
    }
}
