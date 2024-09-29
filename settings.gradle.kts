rootProject.name = "mahjong-utils"

include(":mahjong-utils")
include(":mahjong-utils-entry")
include(":mahjong-utils-webapi")
include(":mahjong-utils-benchmark")

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
