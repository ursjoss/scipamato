description = "SciPaMaTo-Core :: Persistence API Project"

dependencies {
    api(project(":common-persistence-api"))
    api(project(Module.scipamatoCore("pubmed-api")))
    implementation(project(Module.scipamatoCore("entity")))
    implementation(project(":common-utils"))

    testImplementation(project(":common-test"))
}
