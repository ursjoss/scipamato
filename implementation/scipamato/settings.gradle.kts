rootProject.name = "scipamato"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

include(*Module.scipamatoCommonProjects("test", "utils", "entity", "persistence-api", "persistence-jooq-test", "persistence-jooq", "wicket"))

include(*Module.scipamatoCoreProjects("entity", "logic",  "pubmed-api" )) // , "persistence-api", "persistence-jooq", "sync", "web"

include(*Module.scipamatoPublicProjects("entity", "persistence-api", "persistence-jooq", "web"))