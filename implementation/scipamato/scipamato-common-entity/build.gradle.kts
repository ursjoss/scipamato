description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(":scipamato-common-utils"))

    implementation(Lib.commonsCollection())

    testImplementation(Lib.spring("context"))
    testImplementation(project(":scipamato-common-test"))
}