description = "SciPaMaTo-Common :: Test Project"

dependencies {
    api(Lib.assertj())
    api(Lib.springBootStarter("test").id) {
        exclude("junit", "junit")
        exclude("org.skyscreamer", "jsonassert")
    }
    api(Lib.junit5())
    api(Lib.junit5("params"))
    api(Lib.mockito2("core"))
    api(Lib.mockito2("junit-jupiter"))
    api(Lib.equalsverifier())

    implementation(Lib.commonsIo())

    implementation(project(Lib.scipamatoCommon("utils")))
}