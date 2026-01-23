import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "SciPaMaTo-Core :: Web GUI Project"

plugins {
    alias(libs.plugins.springBoot).apply(true)
    id("application-properties-filter")
}

/**
 * Make the static wicket resources that reside next to the java classes in src{main,test} available.
 */
sourceSets {
    /** main: html, css, sass, properties files */
    main {
        resources {
            srcDir("src/main/java")
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
        mainClass.set("ch.difty.scipamato.core.ScipamatoCoreApplicationKt")
        launchScript()
    }
}

dependencies {
    implementation(project(":common-utils"))
    implementation(project(":core-entity"))
    implementation(project(":core-logic"))
    implementation(project(":core-persistence-jooq"))
    implementation(project(":core-pubmed-api"))
    implementation(project(":core-sync"))
    implementation(project(":common-wicket"))

    annotationProcessor(libs.spring.boot.configurationprocessor) {
        exclude("om.vaadin.external.google", "android-json")
    }
    implementation(libs.spring.boot.starter.security)
    implementation(libs.coroutines.core)

    implementation(libs.wicketJqueryUi)
    implementation(libs.wicketJqueryUi.theme.uilightness)
    implementation(libs.wicketstuff.jasperreports) {
        exclude("net.sf.jasperreports", "jasperreports")
        exclude("commons-logging", "commons-logging")
    }
    implementation(libs.jasperreports) {
        exclude("commons-lang", "commons-lang")
        exclude("commons-collections", "commons-collections")
        exclude("commons-logging", "commons-logging")
    }
    implementation(libs.jasperreports.fonts)
    implementation(libs.univocity)
    // temporarily needed until guava has been fully removed from wicket-bootstrap
    implementation("com.google.guava:guava:33.5.0-jre")

    implementation(libs.kris.core)

    /** Caching: JCache with ehcache as cache provider */
    implementation(libs.bundles.caching)
    implementation(libs.ehcache) {
        capabilities {
            requireCapability("org.ehcache:ehcache-jakarta")
        }
    }
    testImplementation(testFixtures(project(":common-wicket")))
    testImplementation(testFixtures(project(":common-persistence-jooq")))
    testImplementation(libs.jakarta.servletApi)
    testImplementation(libs.validationApi)
    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

idea {
    module {
        inheritOutputDirs = true
    }
}
