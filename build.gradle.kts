import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.jetbrains.dokka) apply false
    alias(libs.plugins.devPetsuka.npmPublish) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.kotlinx.kover)
}

group = property("group").toString()
version = property("version").toString()

tasks.wrapper {
    gradleVersion = "8.4"
    distributionType = Wrapper.DistributionType.ALL
}


rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.the<NodeJsRootExtension>().apply {
        nodeVersion = "21.0.0-v8-canary20231024d0ddc81258"
        nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
    }
}

dependencies {
    kover(project(":mahjong-utils"))
    kover(project(":mahjong-utils-entry"))
}
