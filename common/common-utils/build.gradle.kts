description = "SciPaMaTo-Common :: Utilities Project"

dependencies {
    implementation(Lib.commonsLang3())
    testImplementation(project(Module.scipamatoCommon("test")))
}