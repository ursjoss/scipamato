import org.springframework.boot.gradle.tasks.bundling.BootJar
import plugins.ApplicationPropertiesFilterPlugin

description = "SciPaMaTo-Core :: Web GUI Project"

plugins {
    Lib.springBootPlugin().run { id(id) } apply true
}

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
            srcDir("src/test/kotlin")
        }
    }
}

tasks {
    apply<ApplicationPropertiesFilterPlugin>()
    withType<BootJar> {
        enabled = true
        mainClass.set("ch.difty.scipamato.core.ScipamatoCoreApplicationKt")
        launchScript()
    }
}

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoCore("entity")))
    implementation(project(Module.scipamatoCore("logic")))
    implementation(project(Module.scipamatoCore("persistence-jooq")))
    implementation(project(Module.scipamatoCore("pubmed-api")))
    implementation(project(Module.scipamatoCore("sync")))
    implementation(project(Module.scipamatoCommon("wicket")))

    annotationProcessor(Lib.springBoot("configuration-processor").id) {
        exclude("om.vaadin.external.google", "android-json")
    }
    implementation(Lib.springBootStarter("security"))
    implementation(Lib.kotlinCoroutines("core"))

    implementation(Lib.wicketJqueryUi())
    implementation(Lib.wicketJqueryUi("theme-uilightness"))
    implementation(Lib.wicketStuff("jasperreports").id) {
        exclude("net.sf.jasperreports", "jasperreports")
        exclude("commons-logging", "commons-logging")
    }
    implementation(Lib.jasperreports().id) {
        exclude("commons-lang", "commons-lang")
        exclude("commons-collections", "commons-collections")
        exclude("commons-logging", "commons-logging")
    }
    implementation(Lib.jasperreports("fonts"))
    implementation(Lib.univocity())

    implementation(Lib.kris("core"))

    /** Caching: JCache with ehcache as cache provider */
    implementation(Lib.springBootStarter("cache"))
    implementation(Lib.cacheApi())
    implementation(Lib.ehcache())

    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(project(Module.scipamatoCommon("persistence-jooq-test")))
    testImplementation(Lib.servletApi())
    testImplementation(Lib.validationApi())
    testImplementation(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())
}

idea {
    module {
        inheritOutputDirs = true
    }
}
