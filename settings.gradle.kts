import java.util.Properties

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

val envPropFile = file("env.properties")
if (envPropFile.exists()) {
    val props = Properties().apply {
        envPropFile.reader().use { rd ->
            load(rd)
        }
    }
    props.forEach { (k, v) ->
        extra.set(k.toString(), v)
    }
}