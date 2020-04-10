description = "SciPaMaTo-Common :: Persistence API Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoCommon("entity")))

    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(project(Module.scipamatoCommon("utils")))
}
