description = "SciPaMaTo-Common :: Utilities Project"

dependencies {
    implementation(Lib.commonsLang3())
    implementation(Lib.commonsIo())

    testImplementation(project(Module.scipamatoCommon("test")))
}