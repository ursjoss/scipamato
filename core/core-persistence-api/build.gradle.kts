description = "SciPaMaTo-Core :: Persistence API Project"

dependencies {
    api(project(Module.scipamatoCommon("persistence-api")))
    api(project(Module.scipamatoCore("pubmed-api")))
    implementation(project(Module.scipamatoCore("entity")))
    implementation(project(Module.scipamatoCommon("utils")))

    testImplementation(project(Module.scipamatoCommon("test")))
}