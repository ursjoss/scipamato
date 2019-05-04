description = "SciPaMaTo-Public :: Persistence API Project"

dependencies {
    implementation(project(Lib.scipamatoCommon("utils")))
    implementation(project(Lib.scipamatoCommon("persistence-api")))
    implementation(project(Lib.scipamatoPublic("entity")))
}
