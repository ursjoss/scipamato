description = "SciPaMaTo-Core :: Entity Project"

dependencies {
    api(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    api(libs.spring.boot.starter.validation)

    testImplementation(project(Module.scipamatoCommon("test")))
    testImplementation(libs.hibernate.validator)
    testImplementation(libs.javax.el.api)
    testImplementation(libs.javax.el.impl)
}
