description = "SciPaMaTo-Public :: Entity Project"

dependencies {
    api(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))
    testCompile(project(Module.scipamatoCommon("test")))
}