description = "SciPaMaTo-Common :: Persistence API Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoCommon("entity")))

    testCompile(project(Module.scipamatoCommon("test")))
    testCompile(project(Module.scipamatoCommon("utils")))
}
