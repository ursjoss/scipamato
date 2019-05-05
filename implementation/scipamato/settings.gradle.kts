rootProject.name = "scipamato"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

include(*Lib.scipamatoCommonProjects("test", "utils", "entity", "persistence-api", "persistence-jooq", "wicket"))

include(*Lib.scipamatoPublicProjects("entity", "persistence-api"))