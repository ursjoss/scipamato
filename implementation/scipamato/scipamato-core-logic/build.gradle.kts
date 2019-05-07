description = "SciPaMaTo-Core :: Business Logic Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoCore("entity")))

    implementation(Lib.validationApi())

    testCompile(project(Module.scipamatoCommon("test")))
}
