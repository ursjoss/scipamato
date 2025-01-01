description = "SciPaMaTo-Core :: Synchronization Project"

plugins {
    id("scipamato-adhoc-test")
}

/**
 * Make the DB classes generated by jOOQ available in the current project.
 */
val generatedSourcesPath = "build/generated-src/jooq"
sourceSets {
    main {
        java {
            setSrcDirs(setOf(generatedSourcesPath, "src/main/kotlin"))
        }
    }
}

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(libs.spring.boot.starter.batch)
    implementation(libs.spring.boot.starter.jooq)
    annotationProcessor(libs.spring.boot.configurationprocessor) {
        exclude("com.vaadin.external.google", "android-json")
    }

    runtimeOnly(libs.postgresql)
    implementation(libs.jooq)

    testImplementation(project(Module.scipamatoCommon("persistence-jooq-test")))
    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(libs.jooq)

    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)

//    integrationTestRuntimeOnly(libs.postgresql)
}

tasks {
    val path = "build/generated-src/jooq/ch/difty/scipamato"
    val copyCoreFiles by registering(Copy::class) {
        from("$rootDir/core/core-persistence-jooq/$path/core")
        destinationDir = File("$rootDir/core/core-sync/$path/core")
        dependsOn(":core-persistence-jooq:generateJooq")
    }

    val copyPublicFiles by registering(Copy::class) {
        from("$rootDir/public/public-persistence-jooq/$path/publ")
        destinationDir = File("$rootDir/core/core-sync/$path/publ")
        dependsOn(":public-persistence-jooq:generateJooq")
    }

    getByName("compileKotlin").dependsOn += copyCoreFiles
    getByName("compileKotlin").dependsOn += copyPublicFiles
}
