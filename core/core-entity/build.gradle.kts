description = "SciPaMaTo-Core :: Entity Project"

dependencies {
    api(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    api(Lib.springBootStarter("validation"))
    api(Lib.validationApi())

    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(Lib.hibernateValidator())
    testImplementation(Lib.javaxElApi())
    testImplementation(Lib.javaxElImpl())
}
