plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(project(":mahjong-utils"))
    api(libs.kotlinx.serialization.json)
}