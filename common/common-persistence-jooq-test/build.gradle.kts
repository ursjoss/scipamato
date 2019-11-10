description = "SciPaMaTo-Common :: Persistence jOOQ Test Project"

dependencies {
    implementation(project(Module.scipamatoCommon("entity")))
    implementation(project(Module.scipamatoCommon("test")))
    implementation(project(Module.scipamatoCommon("persistence-jooq")))
}
