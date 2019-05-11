rootProject.name = "scipamato"

pluginManagement {
    repositories {
        @Suppress("UnstableApiUsage")
        gradlePluginPortal()
    }
}

val commonProjects = listOf("test", "utils", "entity", "persistence-api", "persistence-jooq-test", "persistence-jooq", "wicket")
val coreProjects = listOf("entity", "logic", "pubmed-api", "persistence-api", "persistence-jooq", "sync", "web")
val publicProjects = listOf("entity", "persistence-api", "persistence-jooq", "web")

include(*Module.scipamatoCommonProjects(commonProjects))
include(*Module.scipamatoCoreProjects(coreProjects))
include(*Module.scipamatoPublicProjects(publicProjects))

defineProjectPaths()

fun defineProjectPaths() {
    mapOf(
            "common" to commonProjects,
            "core" to coreProjects,
            "public" to publicProjects
    ).forEach { (subDir, projects) ->
        projects.forEach { projectName ->
            project(subDir.getPath(projectName)).projectDir = file("$subDir/$projectName")
        }
    }
}

fun String.getPath(project: String): String = when (this) {
    "common" -> Module.scipamatoCommon(project)
    "core" -> Module.scipamatoCore(project)
    "public" -> Module.scipamatoPublic(project)
    else -> throw IllegalArgumentException("project $project is not handled...")
}