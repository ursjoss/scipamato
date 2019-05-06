rootProject.name = "scipamato"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

include(*Module.scipamatoCommonProjects("test", "utils", "entity", "persistence-api", "persistence-jooq-test", "persistence-jooq", "wicket"))

include(*Module.scipamatoPublicProjects("entity", "persistence-api", "persistence-jooq", "web"))