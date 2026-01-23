description = "SciPaMaTo-Public :: Entity Project"

dependencies {
    api(project(":common-entity"))
    implementation(project(":common-utils"))

    testImplementation(libs.equalsverifier)
}
