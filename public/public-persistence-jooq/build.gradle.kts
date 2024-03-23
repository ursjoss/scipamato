@file:Suppress("SpellCheckingInspection")

import ch.ayedo.jooqmodelator.gradle.JooqModelatorTask

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jooqModelator)
    id("scipamato-integration-test")
}

description = "SciPaMaTo-Public:: Persistence jOOQ Project"

val moduleName = "public/public-persistence-jooq"
val dbPackageName = "ch.difty.scipamato.publ.db"
val dbPackagePath get() = dbPackageName.replace('.', '/')
val generatedSourcesPath = "build/generated-src/jooq"
val jooqConfigFile = layout.buildDirectory.get().asFile.resolve("jooqConfig.xml")
val dockerDbPort = 15432
val props = file("src/integration-test/resources/application.properties").asProperties()

testing {
    suites {
        @Suppress("unused")
        val integrationTest by existing {
            dependencies {
                implementation(libs.bundles.dbTest)
                runtimeOnly(libs.postgresql)
            }
        }
    }
}

jooqModelator {
    jooqVersion = dependencyManagement.managedVersions["org.jooq:jooq"]
    jooqEdition = "OSS"

    jooqConfigPath = jooqConfigFile.absolutePath
    jooqOutputPath = "$generatedSourcesPath/$dbPackagePath"

    migrationEngine = "FLYWAY"
    migrationsPaths = listOf("$rootDir/$moduleName/src/main/resources/db/migration/")

    dockerTag = "postgres:15.4"

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

    jooqModelatorRuntime(libs.postgresql)
    runtimeOnly(libs.postgresql)
    api(libs.jooq)

    testImplementation(project(Module.scipamatoCommon("persistence-jooq-test")))
    testImplementation(project(Module.scipamatoCommon("test")))
}

sourceSets {
    main {
        java {
            srcDir(setOf(generatedSourcesPath, "src/main/kotlin"))
        }
    }
}

tasks {
    val jooqGroup = "jOOQ"
    val jooqMetamodelTaskName = "generateJooqMetamodel"
    val generateJooqConfig by creating {
        group = jooqGroup
        description = "Generates the jooqConfig.xml file to be consumed by the $jooqMetamodelTaskName task."
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
        doLast {
            jooqConfigFile.writeText(fileContent)
        }
    }

    withType<JooqModelatorTask> {
        group = jooqGroup
        // prevent parallel run of this task between core and public
        outputs.dir(rootProject.layout.buildDirectory.get().asFile.resolve(jooqMetamodelTaskName))
        dependsOn(generateJooqConfig)
    }
    getByName("compileKotlin").dependsOn += jooqMetamodelTaskName
}

idea {
    module {
        inheritOutputDirs = true
    }
}
