description = "SciPaMaTo-Core :: Entity Project"

dependencies {
    api(project(":common-entity"))
    implementation(project(":common-utils"))

    api(libs.spring.boot.starter.validation)

    testImplementation(project(":common-test"))
    testImplementation(libs.hibernate.validator)
}
