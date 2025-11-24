description = "SciPaMaTo-Common :: Wicket Project"

/**
 * Make the static wicket resources that reside next to the kotlin classes in src{main,test} available.
 */
sourceSets {
    /** main: html, css, properties files */
    main {
        resources {
            setSrcDirs(setOf("src/main/kotlin"))
        }
    }
    /** test: html and properties files */
    test {
        resources {
            setSrcDirs(setOf("src/test/resources", "src/test/kotlin"))
        }
    }
}

dependencies {
    implementation(project(":common-utils"))
    implementation(project(":common-entity"))
    implementation(project(":common-persistence-api"))

    api(libs.spring.boot.starter.undertow)
    api(libs.spring.boot.starter.actuator)
    api(libs.spring.boot.starter.security)
    annotationProcessor(libs.spring.boot.configurationprocessor) {
        exclude("com.vaadin.external.google", "android-json")
    }
    api(libs.spring.boot.admin.starter.client)
    api(libs.spring.core)

    api(libs.spring.boot.starter.web) {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api(libs.spring.boot.starter.wicket) {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api(libs.wicket.core)
    api(libs.wicket.ioc)
    api(libs.wicket.extensions)
    api(libs.wicket.request)
    api(libs.wicket.spring)
    api(libs.wicket.authroles)
    api(libs.wicket.beanvalidation)
    api(libs.hibernate.validator)
    api(libs.wicket.devutils)
    api(libs.wicket.tester)
    api(libs.wicketstuff.annotation)
    api(libs.wicketBootstrap.core)
    api(libs.wicketBootstrap.extensions)
    api(libs.wicketBootstrap.sass)
    api(libs.wicketBootstrap.themes)
    api(libs.fontAwesome)

    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testImplementation(project(":common-test"))

    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testImplementation(libs.jakarta.servletApi)
    testImplementation(libs.validationApi)
}
