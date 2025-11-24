description = "SciPaMaTo-Public :: Persistence API Project"

dependencies {
    api(project(":common-persistence-api"))
    implementation(project(Module.scipamatoPublic("entity")))
    implementation(project(":common-utils"))

    testImplementation(project(":common-test"))
}
