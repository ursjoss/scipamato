description = "SciPaMaTo-Core :: Business Logic Project"

dependencies {
    implementation(project(":common-utils"))
    implementation(project(Module.scipamatoCore("entity")))

    testImplementation(project(":common-test"))
}
