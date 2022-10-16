description = "SciPaMaTo-Common :: Entity Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))

    api(libs.validationApi)

    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(libs.spring.context)
}
