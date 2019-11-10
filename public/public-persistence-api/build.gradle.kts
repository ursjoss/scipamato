description = "SciPaMaTo-Public :: Persistence API Project"

dependencies {
    api(project(Module.scipamatoCommon("persistence-api")))
    implementation(project(Module.scipamatoPublic("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    testCompile(project(Module.scipamatoCommon("test")))
}
