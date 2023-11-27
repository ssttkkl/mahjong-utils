rootProject.name = "mahjong-utils"

include(":mahjong-utils")
include(":mahjong-utils-entry")
include(":mahjong-utils-webapi")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
