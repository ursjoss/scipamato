import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "SciPaMaTo-Public :: Web Project"

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.springBoot).apply(true)
    id("application-properties-filter")
//    id("scipamato-integration-test")
}

//testing {
//    suites {
//        val integrationTest by existing {
//            dependencies {
//                implementation(libs.bundles.dbTest)
//                runtimeOnly(libs.postgresql)
//            }
//        }
//    }
//}

/**
 * Make the static wicket resources that reside next to the java classes in src{main,test} available.
 */
sourceSets {
    /** main: html, css, sass, properties files */
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

    annotationProcessor(libs.spring.boot.configurationprocessor) {
        exclude("om.vaadin.external.google", "android-json")
    }
    implementation(libs.spring.boot.starter.security)

    implementation(libs.bundles.caching)
    implementation(libs.ehcache) {
        capabilities {
            requireCapability("org.ehcache:ehcache-jakarta")
        }
    }
    runtimeOnly(libs.jaxb.runtime)

    testImplementation(project(Module.scipamatoCommon("test")))
}
