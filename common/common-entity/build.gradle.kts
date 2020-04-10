description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))

    implementation(Lib.commonsLang3())
    implementation(Lib.commonsCollection())

    testImplementation(project(Module.scipamatoCommon("test")))

    testImplementation(Lib.spring("context"))
}