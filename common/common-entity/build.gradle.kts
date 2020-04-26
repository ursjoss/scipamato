description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))

    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(Lib.spring("context"))
}