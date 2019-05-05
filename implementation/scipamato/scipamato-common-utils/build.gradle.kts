description = "SciPaMaTo-Common :: Utilities Project"

dependencies {
    implementation(Lib.commonsLang3())
    implementation(Lib.commonsIo())

    testCompile(project(Module.scipamatoCommon("test")))
}