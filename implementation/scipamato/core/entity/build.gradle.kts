description = "SciPaMaTo-Core :: Entity Project"

dependencies {
    api(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    api(Lib.validationApi())

    implementation(Lib.commonsCollection())

    testCompile(project(Module.scipamatoCommon("test")))

    testCompile(Lib.bval())
}
