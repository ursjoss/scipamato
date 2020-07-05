description = "SciPaMaTo-Common :: Test Project"

dependencies {
    api(project(Module.scipamatoCommon("utils")))
    api(project(Module.scipamatoCommon("entity")))

    api(Lib.springBootStarter("test").id) {
        exclude("junit", "junit")
        exclude("org.skyscreamer", "jsonassert")
        exclude("org.mockito", "mockito-core")
        exclude("org.mockito", "mockito-junit-jupiter")
        exclude("org.hamcrest", "hamcrest")
        exclude("org.assertj", "assertj-core")
    }
    api(Lib.junit5())
    api(Lib.junit5("params"))
    api(Lib.mockk())
    api(Lib.kluent().id) {
        exclude("org.mockito", "mockito-core")
        exclude("com.nhaarman.mockitokotlin2", "mockito-kotlin")
    }
    api(Lib.equalsverifier())
    api(Lib.wicket("core"))
}
