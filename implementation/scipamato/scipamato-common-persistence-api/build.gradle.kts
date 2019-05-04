description = "SciPaMaTo-Common :: Persistence API Project"

plugins {
    `java-library`
}

dependencies {
    api(project(Lib.scipamatoCommon("entity")))

    implementation(project(Lib.scipamatoCommon("utils")))

    testCompile(project(Lib.scipamatoCommon("test")))
}
