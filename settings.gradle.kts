rootProject.name = "mahjong-utils"

include(":mahjong-utils")
include(":mahjong-utils-entry")

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
