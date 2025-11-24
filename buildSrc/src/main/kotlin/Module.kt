object Module {

    fun scipamatoCommonProjects(modules: List<String>) = modules.map { "common-$it" }.toTypedArray()

    fun scipamatoCore(module: String) = ":core-$module"
    fun scipamatoCoreProjects(modules: List<String>) = modules.map { "core-$it" }.toTypedArray()

    fun scipamatoPublic(module: String) = ":public-$module"
    fun scipamatoPublicProjects(modules: List<String>) = modules.map { "public-$it" }.toTypedArray()
}
