import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.jetbrains.dokka) apply false
    alias(libs.plugins.devPetsuka.npmPublish) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.kotest.multiplatform) apply false
    alias(libs.plugins.kotlinx.kover)
}

group = property("group").toString()
version = property("version").toString()

tasks.wrapper {
    gradleVersion = "8.4"
    distributionType = Wrapper.DistributionType.ALL
}

dependencies {
    kover(project(":mahjong-utils"))
    kover(project(":mahjong-utils-entry"))
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
