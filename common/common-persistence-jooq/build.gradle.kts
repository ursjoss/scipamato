description = "SciPaMaTo-Common :: Persistence jOOQ Project"

plugins {
    `java-library`
    `java-test-fixtures`
    `maven-publish`
}

dependencies {
    api(libs.spring.boot.starter.jooq)
    api(project(":common-persistence-api"))
    api(libs.flyway.core)

    implementation(libs.flyway.postgresql)

    runtimeOnly(libs.postgresql)

    implementation(project(":common-entity"))
    implementation(project(":common-utils"))

    api(project(":common-utils"))
    api(project(":common-entity"))

    testFixturesApi(libs.junitJupiter.api)
    testFixturesApi(project(":common-entity"))
    testFixturesApi(libs.kluent) {
        exclude("org.mockito", "mockito-core")
        exclude("com.nhaarman.mockitokotlin2", "mockito-kotlin")
    }
}
