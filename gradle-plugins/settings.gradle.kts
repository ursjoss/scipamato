import org.ajoberstar.reckon.gradle.ReckonExtension

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.ajoberstar.reckon.settings") version "2.0.0"
}

configure<ReckonExtension> {
    setDefaultInferredScope("patch")
    stages("rc", "final")
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include("run")
include("verification")
