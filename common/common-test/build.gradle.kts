description = "SciPaMaTo-Common :: Test Project"

dependencies {
    api(project(":common-utils"))
    api(project(":common-entity"))

    api(libs.spring.boot.starter.test) {
        exclude("junit", "junit")
        exclude("org.skyscreamer", "jsonassert")
        exclude("org.mockito", "mockito-core")
        exclude("org.mockito", "mockito-junit-jupiter")
        exclude("org.hamcrest", "hamcrest")
        exclude("org.assertj", "assertj-core")
    }
    api(libs.junitJupiter.api)
    api(libs.junitJupiter.params)
    api(libs.mockk)
    api(libs.kluent) {
        exclude("org.mockito", "mockito-core")
        exclude("com.nhaarman.mockitokotlin2", "mockito-kotlin")
    }
    api(libs.equalsverifier)
    api(libs.wicket.core)
}
