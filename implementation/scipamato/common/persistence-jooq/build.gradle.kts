description = "SciPaMaTo-Common :: Persistence jOOQ Project"

dependencies {
    api(Lib.springBootStarter("jooq"))
    api(project(Module.scipamatoCommon("persistence-api")))
    api(Lib.flyway())
    api(Lib.jool())

    runtimeOnly(Lib.postgres())

    implementation(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    testLibApi(project(Module.scipamatoCommon("test")))
    testLibApi(project(Module.scipamatoCommon("persistence-jooq-test")))
}

