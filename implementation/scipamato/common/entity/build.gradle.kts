description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))

    implementation(Lib.commonsLang3())
    implementation(Lib.commonsCollection())

    testCompile(project(Module.scipamatoCommon("test")))

    testCompile(Lib.spring("context"))
}