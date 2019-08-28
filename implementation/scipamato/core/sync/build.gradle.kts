description = "SciPaMaTo-Core :: Synchronization Project"

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
    implementation(Lib.springBootStarter("batch"))
    implementation(Lib.springBootStarter("jooq"))
    api(Lib.springBoot("configuration-processor").id) {
        exclude("com.vaadin.external.google", "android-json")
    }

    runtimeOnly(Lib.postgres())
    implementation(Lib.jOOQ("jooq"))

    testLibCompile(project(Module.scipamatoCommon("persistence-jooq-test")))
    testLibCompile(project(Module.scipamatoCommon("test")))
    testLibCompile(Lib.jOOQ("jooq"))

    testCompile(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

    integrationTestRuntimeOnly(Lib.postgres())
}

tasks {
    val copyCoreFiles by registering(Copy::class) {
        from("$rootDir/core/persistence-jooq/build/generated-src/jooq/ch/difty/scipamato/core")
        destinationDir = File("$rootDir/core/sync/build/generated-src/jooq/ch/difty/scipamato/core")
        dependsOn(":core-persistence-jooq:generateJooqMetamodel")
    }

    val copyPublicFiles by registering(Copy::class) {
        from("$rootDir/public/persistence-jooq/build/generated-src/jooq/ch/difty/scipamato/publ")
        destinationDir = File("$rootDir/core/sync/build/generated-src/jooq/ch/difty/scipamato/publ")
        dependsOn(":public-persistence-jooq:generateJooqMetamodel")
    }

    getByName("compileKotlin").dependsOn += copyCoreFiles
    getByName("compileKotlin").dependsOn += copyPublicFiles
}