description = "SciPaMaTo-Common :: Utilities Project"

plugins {
    `java-library`
}

dependencies {
    api(Lib.commonsLang3())

    implementation(Lib.commonsIo())

    testImplementation(project(Lib.scipamatoCommon("test")))

    testImplementation(Lib.spring("core"))
}