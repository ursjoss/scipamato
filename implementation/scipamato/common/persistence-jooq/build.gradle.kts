description = "SciPaMaTo-Common :: Persistence jOOQ Project"

dependencies {
    api(Lib.springBootStarter("jooq"))
    api(Lib.jOOQ())
    api(project(Module.scipamatoCommon("persistence-api")))
    api(Lib.flyway())
    api(Lib.jool())
    // TODO move this into scipamato-core-persistence-jooq once it's migrated to gradle
    api(Lib.springSecurity("core"))

    runtimeOnly(Lib.postgres())

    implementation(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    testLibApi(project(Module.scipamatoCommon("test")))
    testLibApi(project(Module.scipamatoCommon("persistence-jooq-test")))

    testLibApi(Lib.bval())
}

