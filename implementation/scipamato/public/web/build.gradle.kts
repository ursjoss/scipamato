import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    Lib.springBootPlugin().run { id(id) } apply true
}

description = "SciPaMaTo-Public :: Web Project"

/** Content filtering of application properties */
apply(from = "$rootDir/gradle/filterApplicationProperties.gradle")

/**
 * Make the static wicket resources that reside next to the java classes in src{main,test} available.
 */
sourceSets {
    /** main: html, css, less, properties files */
    main {
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

tasks {
    withType<BootJar> {
        enabled = true
        mainClassName = "ch.difty.scipamato.publ.ScipamatoPublicApplication"
        launchScript()
    }
}

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoPublic("entity")))
    implementation(project(Module.scipamatoPublic("persistence-jooq")))
    implementation(project(Module.scipamatoCommon("wicket")))

    implementation(Lib.springBoot("configuration-processor").id) {
        exclude("om.vaadin.external.google", "android-json")
    }
    implementation(Lib.springBootStarter("security"))

    implementation(Lib.springBootStarter("cache"))
    implementation(Lib.cacheApi())
    implementation(Lib.ehcache())

    implementation(Lib.jaxbApi())
    implementation(Lib.jaxbCore())
    implementation(Lib.jaxb("impl"))
    implementation(Lib.javaxActivation())
    implementation(Lib.jacksonKotlin())

    implementation(Lib.validationApi())

    testCompile(project(Module.scipamatoCommon("test")))
    testCompile(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())
}
