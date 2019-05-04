description = "SciPaMaTo-Common :: Persistence jOOQ Project"

plugins {
    `java-library`
}

dependencies {
    implementation(project(Lib.scipamatoCommon("entity")))
    implementation(project(Lib.scipamatoCommon("persistence-api")))
    implementation(project(Lib.scipamatoCommon("utils")))

    implementation(Lib.springBootStarter("security"))
    implementation(Lib.springBootStarter("jooq"))

    implementation(Lib.jOOQ())
    implementation(Lib.flyway())
    implementation(Lib.postgres())

    implementation(Lib.jool())

    testImplementation(project(Lib.scipamatoCommon("test")))

    testCompile(Lib.bval())
}

