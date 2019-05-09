object Module {

    fun scipamatoCommon(module: String) = ":common-$module"
    fun scipamatoCommonProjects(vararg modules: String) = modules.map { "common-$it" }.toTypedArray()

    fun scipamatoCore(module: String) = ":core-$module"
    fun scipamatoCoreProjects(vararg modules: String) = modules.map { "core-$it" }.toTypedArray()

    fun scipamatoPublic(module: String) = ":public-$module"
    fun scipamatoPublicProjects(vararg modules: String) = modules.map { "public-$it" }.toTypedArray()
}
