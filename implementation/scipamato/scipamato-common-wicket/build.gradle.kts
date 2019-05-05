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
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("persistence-api")))

    api(Lib.springBootStarter("web"))
    api(Lib.springBootStarter("undertow"))
    api(Lib.springBootStarter("actuator"))
    api(Lib.springBootStarter("security"))
    api(Lib.springBoot("configuration-processor"))
    api(Lib.springBootAdmin())
    api(Lib.spring("core"))

    api(Lib.springBootStarterWicket())
    api(Lib.wicket("core"))
    api(Lib.wicket("ioc"))
    api(Lib.wicket("extensions"))
    api(Lib.wicket("request"))
    api(Lib.wicket("spring"))
    api(Lib.wicket("auth-roles"))
    api(Lib.wicket("bean-validation"))
    api(Lib.wicket("devutils"))
    api(Lib.wicketStuff("annotation"))
    api(Lib.wicketBootstrap("core"))
    api(Lib.wicketBootstrap("extensions"))
    api(Lib.wicketBootstrap("less"))
    api(Lib.wicketBootstrap("themes"))
    api(Lib.fontAwesome())

    testCompile(project(Module.scipamatoCommon("test")))

    testCompile(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

    testCompile(Lib.servletApi())
    testCompile(Lib.validationApi())
    testCompile(Lib.bval())
}
