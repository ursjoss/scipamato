description = "SciPaMaTo-Common :: Persistence jOOQ Project"

dependencies {
    api(libs.spring.boot.starter.jooq)
    api(project(Module.scipamatoCommon("persistence-api")))
    api(libs.flyway.core)
    api(libs.flyway.postgresql)

    runtimeOnly(libs.postgresql)

    implementation(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    testApi(project(Module.scipamatoCommon("test")))
    testApi(project(Module.scipamatoCommon("persistence-jooq-test")))
}
