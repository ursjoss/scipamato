import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "SciPaMaTo-Public :: Web Project"

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
            setSrcDirs(setOf("src/main/resources", "src/main/kotlin"))
        }
    }
    /** test: html and properties files */
    test {
        resources {
            setSrcDirs(setOf("src/test/resources", "src/test/kotlin"))
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
    implementation(project(":common-utils"))
    implementation(project(":public-entity"))
    implementation(project(":public-persistence-jooq"))
    implementation(project(":common-wicket"))

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

    testImplementation(project(":common-test"))
}
