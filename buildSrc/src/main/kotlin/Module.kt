object Module {

    fun scipamatoCommonProjects(modules: List<String>) = modules.map { "common-$it" }.toTypedArray()

    fun scipamatoCoreProjects(modules: List<String>) = modules.map { "core-$it" }.toTypedArray()

    fun scipamatoPublicProjects(modules: List<String>) = modules.map { "public-$it" }.toTypedArray()
}
