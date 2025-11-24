description = "SciPaMaTo-Core :: Persistence API Project"

dependencies {
    api(project(":common-persistence-api"))
    api(project(":core-pubmed-api"))
    implementation(project(":core-entity"))
    implementation(project(":common-utils"))

    testImplementation(project(":common-test"))
}
