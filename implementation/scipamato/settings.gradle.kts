rootProject.name = "scipamato"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

include(*Module.scipamatoCommonProjects("test", "utils", "entity", "persistence-api", "persistence-jooq-test", "persistence-jooq", "wicket"))

include(*Module.scipamatoCoreProjects("entity", "logic", "pubmed-api", "persistence-api", "persistence-jooq", "sync", "web"))

include(*Module.scipamatoPublicProjects("entity", "persistence-api", "persistence-jooq", "web"))

project(Module.scipamatoCommon("test")).projectDir                  = file("common/test")
project(Module.scipamatoCommon("utils")).projectDir                 = file("common/utils")
project(Module.scipamatoCommon("entity")).projectDir                = file("common/entity")
project(Module.scipamatoCommon("persistence-api")).projectDir       = file("common/persistence-api")
project(Module.scipamatoCommon("persistence-jooq-test")).projectDir = file("common/persistence-jooq-test")
project(Module.scipamatoCommon("persistence-jooq")).projectDir      = file("common/persistence-jooq")
project(Module.scipamatoCommon("wicket")).projectDir                = file("common/wicket")

project(Module.scipamatoCore("entity")).projectDir           = file("core/entity")
project(Module.scipamatoCore("logic")).projectDir            = file("core/logic")
project(Module.scipamatoCore("pubmed-api")).projectDir       = file("core/pubmed-api")
project(Module.scipamatoCore("persistence-api")).projectDir  = file("core/persistence-api")
project(Module.scipamatoCore("persistence-jooq")).projectDir = file("core/persistence-jooq")
project(Module.scipamatoCore("sync")).projectDir             = file("core/sync")
project(Module.scipamatoCore("web")).projectDir              = file("core/web")

project(Module.scipamatoPublic("entity")).projectDir           = file("public/entity")
project(Module.scipamatoPublic("persistence-api")).projectDir  = file("public/persistence-api")
project(Module.scipamatoPublic("persistence-jooq")).projectDir = file("public/persistence-jooq")
project(Module.scipamatoPublic("web")).projectDir              = file("public/web")

