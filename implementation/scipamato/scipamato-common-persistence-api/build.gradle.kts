description = "SciPaMaTo-Common :: Persistence API Project"

plugins {
    `java-library`
}

dependencies {
    implementation(project(Lib.scipamatoCommon("utils")))
    implementation(project(Lib.scipamatoCommon("entity")))

    testCompile(project(Lib.scipamatoCommon("test")))
}
