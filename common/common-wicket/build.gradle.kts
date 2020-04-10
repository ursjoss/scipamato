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
            srcDir("src/test/kotlin")
        }
    }
}

configurations {
    val developmentOnly: Configuration = create("developmentOnly")
    runtimeClasspath.get().extendsFrom(developmentOnly)
}

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("persistence-api")))

    api(Lib.springBootStarter("undertow"))
    api(Lib.springBootStarter("actuator"))
    api(Lib.springBootStarter("security"))
    api(Lib.springBoot("configuration-processor").id) {
        exclude("com.vaadin.external.google", "android-json")
    }
    api(Lib.springBootAdmin())
    api(Lib.spring("core"))

    api(Lib.springBootStarter("web").id) {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api(Lib.springBootStarterWicket().id) {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
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
    api(Lib.wicketBootstrap("less").id) {
        exclude("commons-collections", "commons-collections")
        exclude("commons-logging", "commons-logging")
    }
    api(Lib.wicketBootstrap("themes"))
    api(Lib.fontAwesome())

    testImplementation(Lib.lombok())
    testAnnotationProcessor(Lib.lombok())

    testLibCompile(project(Module.scipamatoCommon("test")))

    testLibImplementation(Lib.lombok())
    testLibAnnotationProcessor(Lib.lombok())

    testLibImplementation(Lib.servletApi())
    testLibImplementation(Lib.validationApi())

    developmentOnly(Lib.springBoot("devtools"))
}
