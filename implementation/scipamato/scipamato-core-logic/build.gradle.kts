description = "SciPaMaTo-Core :: Business Logic Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoCore("entity")))

    testCompile(project(Module.scipamatoCommon("test")))
}
