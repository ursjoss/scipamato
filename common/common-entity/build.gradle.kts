description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(":common-utils"))

    api(libs.validationApi)

    testImplementation(libs.spring.context)
    testImplementation(libs.equalsverifier)
}
