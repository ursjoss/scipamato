description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(Lib.scipamatoCommon("utils")))

    implementation(Lib.commonsCollection())

    testImplementation(project(Lib.scipamatoCommon("test")))

    testImplementation(Lib.spring("context"))
}