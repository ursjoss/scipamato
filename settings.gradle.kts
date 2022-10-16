@file:Suppress("UnstableApiUsage", "SpreadOperator")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts") }
    }
}

rootProject.name = "scipamato"

val commonProjects = listOf(
    "test", "utils", "entity", "persistence-api", "persistence-jooq-test", "persistence-jooq", "wicket"
)
val coreProjects = listOf(
    "entity", "logic", "pubmed-api", "persistence-api", "persistence-jooq", "sync", "web"
)
val publicProjects = listOf("entity", "persistence-api", "persistence-jooq", "web")

fun scipamatoCommonProjects(modules: List<String>) = modules.map { "common-$it" }.toTypedArray()
fun scipamatoCoreProjects(modules: List<String>) = modules.map { "core-$it" }.toTypedArray()
fun scipamatoPublicProjects(modules: List<String>) = modules.map { "public-$it" }.toTypedArray()

fun scipamatoCommon(module: String) = "common".scipamatoModule(module)
fun scipamatoCore(module: String) = "core".scipamatoModule(module)
fun scipamatoPublic(module: String) = "public".scipamatoModule(module)

fun String.scipamatoModule(module: String) = ":$this-$module"

include(*scipamatoCommonProjects(commonProjects))
include(*scipamatoCoreProjects(coreProjects))
include(*scipamatoPublicProjects(publicProjects))

defineProjectPaths()

fun defineProjectPaths() {
    mapOf(
        "common" to commonProjects,
        "core" to coreProjects,
        "public" to publicProjects
    ).forEach { (subDir, projects) ->
        projects.forEach { projectName ->
            project(subDir.getPath(projectName)).projectDir = file("$subDir/$subDir-$projectName")
        }
    }
}

fun String.getPath(project: String): String = when (this) {
    "common" -> scipamatoCommon(project)
    "core" -> scipamatoCore(project)
    "public" -> scipamatoPublic(project)
    else -> throw IllegalArgumentException("project $project is not handled...")
}
