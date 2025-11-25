description = "SciPaMaTo-Common :: Persistence jOOQ Project"

dependencies {
    api(libs.spring.boot.starter.jooq)
    api(project(":common-persistence-api"))
    api(libs.flyway.core)

    implementation(libs.flyway.postgresql)

    runtimeOnly(libs.postgresql)

    implementation(project(":common-entity"))
    implementation(project(":common-utils"))

    testApi(project(":common-test"))
    testApi(project(":common-persistence-jooq-test"))
}
