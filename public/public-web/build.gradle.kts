import org.springframework.boot.gradle.tasks.bundling.BootJar
import plugins.ApplicationPropertiesFilterPlugin

plugins {
    Lib.springBootPlugin().run { id(id) } apply true
}

description = "SciPaMaTo-Public :: Web Project"

/**
 * Make the static wicket resources that reside next to the java classes in src{main,test} available.
 */
sourceSets {
    /** main: html, css, less, properties files */
    main {
        resources {
            srcDir("src/main/kotlin")
        }
    }
    /** test: html and properties files */
    test {
        resources {
            srcDir("src/test/kotlin")
        }
    }
}

tasks {
    apply<ApplicationPropertiesFilterPlugin>()
    withType<BootJar> {
        enabled = true
        mainClass.set("ch.difty.scipamato.publ.ScipamatoPublicApplicationKt")
        launchScript()
    }
}

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoPublic("entity")))
    implementation(project(Module.scipamatoPublic("persistence-jooq")))
    implementation(project(Module.scipamatoCommon("wicket")))

    annotationProcessor(Lib.springBoot("configuration-processor").id) {
        exclude("om.vaadin.external.google", "android-json")
    }
    implementation(Lib.springBootStarter("security"))

    implementation(Lib.springBootStarter("cache"))
    implementation(Lib.cacheApi())
    implementation(Lib.ehcache())

    implementation(Lib.jaxbApi())
    implementation(Lib.jaxbCore())
    runtimeOnly(Lib.jaxbRuntime())
    implementation(Lib.jaxb("impl"))
    implementation(Lib.javaxActivation())
    implementation(Lib.jacksonKotlin())

    implementation(Lib.validationApi())

    testImplementation(project(Module.scipamatoCommon("test")))

    integrationTestImplementation(Lib.testcontainers("testcontainers"))
    integrationTestImplementation(Lib.testcontainers("junit-jupiter"))
    integrationTestImplementation(Lib.testcontainers("postgresql"))
    integrationTestRuntimeOnly(Lib.postgres())
}
