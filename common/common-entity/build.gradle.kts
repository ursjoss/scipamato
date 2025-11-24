description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(":common-utils"))

    api(libs.validationApi)

    testImplementation(project(":common-test"))
    testImplementation(libs.spring.context)
}
