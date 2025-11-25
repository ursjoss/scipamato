description = "SciPaMaTo-Core :: Pubmed API Project"

plugins {
    alias(libs.plugins.jaxb)
    id("scipamato-integration-test")
    id("scipamato-adhoc-test")
}

testing {
    suites {
        @Suppress("UNUSED_VARIABLE")
        val integrationTest by existing {
            dependencies {
                annotationProcessor(libs.lombok)
                runtimeOnly(libs.lombok)
            }
        }
    }
}

dependencies {
    implementation(project(":common-utils"))

    // Cloud access
    api(libs.spring.cloud.starter.openfeign) {
        exclude("com.netflix.archaius", "archaius-core")
    }
    implementation(libs.openfeign.jaxb)
    implementation(libs.openfeign.okhttp)
    implementation(libs.openfeign.slf4j)

    // Object/XML mapping
    implementation(libs.spring.oxm)
    implementation(libs.jakarta.bind.api)
    runtimeOnly(libs.jaxb.runtime)

    testImplementation(project(":common-test"))
}

/**
 * Currently this task executes automatically and writes into core/core-pubmed-api/build/generated-sources/jaxb.
 * TODO configure this task to not run unless triggered explicitly with gradlew :core-pubmed-api:jaxbJavaGenPubmed
 * If this works, we can point the outputdir to "$rootDir/core/core-pubmed-api/src/main/java", thus overwriting the existing
 * generated classes.
 */
System.setProperty("enableExternalEntityProcessing", "true")
jaxb {
    javaGen {
        register("pubmed") {
            schema = File("$rootDir/core/core-pubmed-api/src/main/resources/pubmed_180101.dtd")
            language = "DTD"
            header = false
            packageName = "ch.difty.scipamato.core.pubmed.api"
            sourceSetName = ""
            outputDir = File("$rootDir/core/core-pubmed-api/build/generated-sources/jaxb/")
        }
    }
}

idea {
    module {
        inheritOutputDirs = true
    }
}
