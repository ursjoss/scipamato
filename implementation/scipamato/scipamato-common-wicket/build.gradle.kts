description = "SciPaMaTo-Common :: Wicket Project"

/**
 * Make the static wicket resources that reside next to the java classes in src{main,test} available.
 */
sourceSets {
    /** main: html, css, properties files */
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

dependencies {
    implementation(project(Lib.scipamatoCommon("utils")))
    implementation(project(Lib.scipamatoCommon("persistence-api")))

    implementation(Lib.springBootStarter("web"))
    implementation(Lib.springBootStarter("undertow"))
    implementation(Lib.springBootStarter("actuator"))
    implementation(Lib.springBootStarter("security"))
    implementation(Lib.springBoot("configuration-processor"))
    implementation(Lib.springBootAdmin())
    implementation(Lib.spring("core"))

    implementation(Lib.springBootStarterWicket())
    implementation(Lib.wicket("core"))
    implementation(Lib.wicket("ioc"))
    implementation(Lib.wicket("extensions"))
    implementation(Lib.wicket("request"))
    implementation(Lib.wicket("spring"))
    implementation(Lib.wicket("auth-roles"))
    implementation(Lib.wicket("bean-validation"))
    implementation(Lib.wicket("devutils"))
    implementation(Lib.wicketStuff("annotation"))
    implementation(Lib.wicketBootstrap("core"))
    implementation(Lib.wicketBootstrap("extensions"))
    implementation(Lib.wicketBootstrap("less"))
    implementation(Lib.wicketBootstrap("themes"))
    implementation(Lib.fontAwesome())

    testImplementation(project(Lib.scipamatoCommon("test")))

    testImplementation(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

    testImplementation(Lib.servletApi())
    testImplementation(Lib.validationApi())
    testImplementation(Lib.bval())
}
