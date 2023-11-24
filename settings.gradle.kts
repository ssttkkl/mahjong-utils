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
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}
