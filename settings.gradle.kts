@file:Suppress("UnstableApiUsage")

import org.ajoberstar.reckon.gradle.ReckonExtension

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.ajoberstar.reckon.settings") version "1.0.1"
}

configure<ReckonExtension> {
    setDefaultInferredScope("patch")
    stages("rc", "final")
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts") }
        // repository providing ua.kasta:jsass
        maven { url = uri("https://repo.clojars.org") }
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

fun scipamatoCommonProjects(modules: List<String>) = modules.map { "common-$it" }
fun scipamatoCoreProjects(modules: List<String>) = modules.map { "core-$it" }
fun scipamatoPublicProjects(modules: List<String>) = modules.map { "public-$it" }

fun scipamatoCommon(module: String) = "common".scipamatoModule(module)
fun scipamatoCore(module: String) = "core".scipamatoModule(module)
fun scipamatoPublic(module: String) = "public".scipamatoModule(module)

fun String.scipamatoModule(module: String) = ":$this-$module"

include(scipamatoCommonProjects(commonProjects))
include(scipamatoCoreProjects(coreProjects))
include(scipamatoPublicProjects(publicProjects))

includeBuild("gradle-plugins")

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
