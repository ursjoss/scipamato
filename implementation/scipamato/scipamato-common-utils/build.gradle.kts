description = "SciPaMaTo-Common :: Utilities Project"

plugins {
    `java-library`
}

dependencies {
    api(Lib.commonsLang3())
    implementation(Lib.commonsIo())
    testImplementation(Lib.spring("core"))
    testImplementation(project(":scipamato-common-test"))
}