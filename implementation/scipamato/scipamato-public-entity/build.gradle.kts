description = "SciPaMaTo-Public :: Entity Project"

dependencies {
    implementation(project(Lib.scipamatoCommon("utils")))
    implementation(project(Lib.scipamatoCommon("entity")))

    testImplementation(project(Lib.scipamatoCommon("test")))
}