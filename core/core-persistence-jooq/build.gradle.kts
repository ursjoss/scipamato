@file:Suppress("SpellCheckingInspection")

import ch.ayedo.jooqmodelator.gradle.JooqModelatorTask

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jooqModelator)
}

description = "SciPaMaTo-Core :: Persistence jOOQ Project"

val moduleName = "core/core-persistence-jooq"
val dbPackageName = "ch.difty.scipamato.core.db"
val dbPackagePath get() = dbPackageName.replace('.', '/')
val generatedSourcesPath = "build/generated-src/jooq"
val jooqConfigFile = "$buildDir/jooqConfig.xml"
val dockerDbPort = 15432
val props = file("src/integration-test/resources/application.properties").asProperties()

jooqModelator {
    jooqVersion = libs.versions.jooq.get()
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
    api(project(Module.scipamatoCore("persistence-api")))
    api(project(Module.scipamatoCommon("persistence-jooq")))
    implementation(project(Module.scipamatoCore("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    jooqModelatorRuntime(libs.postgresql)
    runtimeOnly(libs.postgresql)
    api(libs.jooq)

    implementation(libs.spring.security.core)

    testImplementation(project(Module.scipamatoCommon("persistence-jooq-test")))
    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(project(Module.scipamatoCore("entity")))

    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)

//    integrationTestImplementation(libs.bundles.dbTest)
//    integrationTestRuntimeOnly(libs.postgresql)
//    integrationTestAnnotationProcessor(libs.lombok)
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
                            <inputSchema>${props.getProperty("db.schema")}</inputSchema>
                            <recordVersionFields>version</recordVersionFields>
                        </database>
                        <generate>
                            <deprecated>false</deprecated>
                            <instanceFields>true</instanceFields>
                            <pojos>false</pojos>
                            <javaTimeTypes>false</javaTimeTypes>
                            <springAnnotations>true</springAnnotations>
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
