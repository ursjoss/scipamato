import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "SciPaMaTo-Core :: Web GUI Project"

plugins {
    Lib.springBootPlugin().run { id(id) } apply true
}

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
        mainClassName = "ch.difty.scipamato.core.ScipamatoCoreApplication"
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

    implementation(Lib.springBootStarter("security"))

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

    /** Caching: JCache with ehcache as cache provider */
    implementation(Lib.springBootStarter("cache"))
    implementation(Lib.cacheApi())
    implementation(Lib.ehcache())

    implementation(Lib.jaxbApi())
    implementation(Lib.jaxbCore())
    implementation(Lib.jaxb("impl"))
    implementation(Lib.javaxActivation())
    implementation(Lib.jaxbRuntime())

    testCompile(project(Module.scipamatoCommon("test")))
    testCompile(project(Module.scipamatoCommon("persistence-jooq-test")))
    testCompile(Lib.servletApi())
    testCompile(Lib.validationApi())
    testCompile(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())
}