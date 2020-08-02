@file:Suppress("SpellCheckingInspection")

import ch.ayedo.jooqmodelator.gradle.JooqModelatorTask

plugins {
    Lib.jooqModelatorPlugin().run { id(id) version version }
}

description = "SciPaMaTo-Public:: Persistence jOOQ Project"

val moduleName = "public/public-persistence-jooq"
val dbPackageName = "ch.difty.scipamato.publ.db"
val dbPackagePath get() = dbPackageName.replace('.', '/')
val generatedSourcesPath = "build/generated-src/jooq"
val jooqConfigFile = "$buildDir/jooqConfig.xml"
val dockerDbPort = 15432
val props = file("src/integration-test/resources/application.properties").asProperties()

jooqModelator {
    jooqVersion = Lib.jooqVersion
    jooqEdition = "OSS"

    jooqConfigPath = jooqConfigFile
    jooqOutputPath = "$generatedSourcesPath/$dbPackagePath"

    migrationEngine = "FLYWAY"
    migrationsPaths = listOf("$rootDir/$moduleName/src/main/resources/db/migration/")

    dockerTag = "postgres:10"

    dockerEnv = listOf(
        "POSTGRES_DB=${props.getProperty("db.name")}",
        "POSTGRES_USER=${props.getProperty("spring.datasource.hikari.username")}",
        "POSTGRES_PASSWORD=${props.getProperty("spring.datasource.hikari.password")}"
    )
    dockerHostPort = dockerDbPort
    dockerContainerPort = props.getProperty("db.port").toInt()
}

dependencies {
    api(project(Module.scipamatoPublic("persistence-api")))
    api(project(Module.scipamatoCommon("persistence-jooq")))
    implementation(project(Module.scipamatoPublic("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    jooqModelatorRuntime(Lib.postgres())
    runtimeOnly(Lib.postgres())
    api(Lib.jOOQ("jooq"))

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
        val fileContent =
            """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <configuration>
                    <jdbc>
                        <driver>org.postgresql.Driver</driver>
                        <url>jdbc:postgresql://localhost:$dockerDbPort/${props.getProperty("db.name")}?loggerLevel=OFF</url>
                        <user>${props.getProperty("spring.datasource.hikari.username")}</user>
                        <password>${props.getProperty("spring.datasource.hikari.password")}</password>
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
                            <pojos>true</pojos><!-- pojos required for sync only -->
                            <springAnnotations>true</springAnnotations>
                            <javaTimeTypes>false</javaTimeTypes>
                        </generate>
                        <target>
                            <packageName>$dbPackageName</packageName>
                            <directory>$moduleName/$generatedSourcesPath</directory>
                        </target>
                    </generator>
                    <basedir>$rootDir</basedir>
                </configuration>
            """.trimIndent()
        file(jooqConfigFile).writeText(fileContent)
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
