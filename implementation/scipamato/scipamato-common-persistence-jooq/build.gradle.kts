description = "SciPaMaTo-Common :: Persistence jOOQ Project"

dependencies {
    api(Lib.springBootStarter("jooq"))
    api(Lib.jOOQ())
    api(project(Module.scipamatoCommon("persistence-api")))
    api(Lib.flyway())

    runtimeOnly(Lib.postgres())

    implementation(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    implementation(Lib.jool())

    testCompile(project(Module.scipamatoCommon("test")))
    testCompile(project(Module.scipamatoCommon("persistence-jooq-test")))

    testCompile(Lib.bval())
}

