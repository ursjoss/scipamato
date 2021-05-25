description = "SciPaMaTo-Common :: Persistence jOOQ Project"

dependencies {
    api(Lib.springBootStarter("jooq"))
    api(project(Module.scipamatoCommon("persistence-api")))
    api(Lib.flyway())

    runtimeOnly(Lib.postgres())

    implementation(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    testApi(project(Module.scipamatoCommon("test")))
    testApi(project(Module.scipamatoCommon("persistence-jooq-test")))
}
