description = "SciPaMaTo-Core :: Pubmed API Project"

plugins {
    Lib.jaxbPlugin().run { id(id) version version }
}

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))

    // Cloud access
    api(Lib.springCloud("openfeign").id) {
        exclude("com.netflix.archaius", "archaius-core")
    }
    implementation(Lib.openfeign("jaxb"))
    implementation(Lib.openfeign("okhttp"))
    implementation(Lib.openfeign("slf4j"))

    // Object/XML mapping
    implementation(Lib.spring("oxm"))
    implementation(Lib.jaxbApi())
    runtimeOnly(Lib.jaxbRuntime())

    testLibCompile(project(Module.scipamatoCommon("test")))

    integrationTestAnnotationProcessor(Lib.lombok())
    integrationTestRuntimeOnly(Lib.lombok())

    jaxb(Lib.jaxbApi())
    jaxb(Lib.jaxbCore())
    jaxb(Lib.javaxActivationApi())
    jaxb(Lib.jaxb("xjc"))
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
