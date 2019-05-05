object Module {

    fun scipamatoCommon(module: String) = ":scipamato-common-$module"
    fun scipamatoCommonProjects(vararg modules: String) = modules.map { "scipamato-common-$it" }.toTypedArray()

    fun scipamatoPublic(module: String) = ":scipamato-public-$module"
    fun scipamatoPublicProjects(vararg modules: String) = modules.map { "scipamato-public-$it" }.toTypedArray()
}