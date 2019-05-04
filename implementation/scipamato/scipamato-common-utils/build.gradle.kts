description = "SciPaMaTo-Common :: Utilities Project"

dependencies {
    api(Lib.commonsLang3())

    implementation(Lib.commonsIo())

    testImplementation(project(Lib.scipamatoCommon("test")))

    testImplementation(Lib.spring("core"))
}