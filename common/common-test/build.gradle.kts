description = "SciPaMaTo-Common :: Test Project"


dependencies {
    api(project(Module.scipamatoCommon("utils")))
    api(project(Module.scipamatoCommon("entity")))

    api(Lib.kluent())
    api(Lib.springBootStarter("test").id) {
        exclude("junit", "junit")
        exclude("org.skyscreamer", "jsonassert")
    }
    api(Lib.junit5())
    api(Lib.junit5("params"))
    api(Lib.equalsverifier())
}
