description = "SciPaMaTo-Core :: Entity Project"

dependencies {
    api(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    implementation(Lib.commonsCollection())
    implementation(Lib.validationApi())

    testCompile(project(Module.scipamatoCommon("test")))

    testCompile(Lib.bval())
}
