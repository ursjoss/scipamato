description = "SciPaMaTo-Core :: Entity Project"

dependencies {
    api(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    api(Lib.springBootStarter("validation"))

    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(Lib.hibernateValidator())
    testImplementation(Lib.javaxElApi())
    testImplementation(Lib.javaxElImpl())
}
