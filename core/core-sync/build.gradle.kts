description = "SciPaMaTo-Core :: Synchronization Project"

plugins{
    id("scipamato-adhoc-test")
}

/**
 * Make the static wicket resources that reside next to the java classes in src{main,test} available.
 */
val generatedSourcesPath = "build/generated-src/jooq"
sourceSets {
    /** main: html, css, less, properties files */
    main {
        java {
            srcDir(setOf(generatedSourcesPath, "src/main/java"))
        }
        resources {
            srcDir("src/main/java")
        }
    }
    /** test: html and properties files */
    test {
        resources {
            srcDir("src/test/java")
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
    val copyCoreFiles by registering(Copy::class) {
        from("$rootDir/core/core-persistence-jooq/build/generated-src/jooq/ch/difty/scipamato/core")
        destinationDir = File("$rootDir/core/core-sync/build/generated-src/jooq/ch/difty/scipamato/core")
        dependsOn(":core-persistence-jooq:generateJooqMetamodel")
    }

    val copyPublicFiles by registering(Copy::class) {
        from("$rootDir/public/public-persistence-jooq/build/generated-src/jooq/ch/difty/scipamato/publ")
        destinationDir = File("$rootDir/core/core-sync/build/generated-src/jooq/ch/difty/scipamato/publ")
        dependsOn(":public-persistence-jooq:generateJooqMetamodel")
    }

    getByName("compileKotlin").dependsOn += copyCoreFiles
    getByName("compileKotlin").dependsOn += copyPublicFiles
}
