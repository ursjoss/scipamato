description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))

    api(Lib.validationApi())

    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(Lib.spring("context"))
}
