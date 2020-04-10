description = "SciPaMaTo-Public :: Entity Project"

dependencies {
    api(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))
    testImplementation(project(Module.scipamatoCommon("test")))
}