description = "SciPaMaTo-Core :: Business Logic Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))
    implementation(project(Module.scipamatoCore("entity")))

    testImplementation(project(Module.scipamatoCommon("test")))
}
