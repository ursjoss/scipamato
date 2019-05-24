plugins {
    Lib.jooqPlugin().run { id(id) version version }
}

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

    jooqRuntime(Lib.postgres())
    runtimeOnly(Lib.postgres())
    implementation(Lib.jOOQ("jooq"))

    testLibCompile(project(Module.scipamatoCommon("persistence-jooq-test")))
    testLibCompile(project(Module.scipamatoCommon("test")))
    testLibCompile(Lib.jOOQ("jooq"))

    testCompile(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

    integrationTestRuntimeOnly(Lib.postgres())
}

val bootSyncProps = file("src/main/resources/application.properties").asProperties()
val bootSyncItProps = file("src/intTest/resources/application.properties").asProperties()


apply(from = "$rootDir/gradle/jooq-core-sync.gradle")