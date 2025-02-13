pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GraphicsTask"
include(":app")
include(":xygraph-impl")
include(":xygraph-api")
include(":photo-saver-api")
include(":photo-saver-impl")
include(":points-count-api")
include(":points-count-impl")
include(":common-ui")
include(":common-network")
