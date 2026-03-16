plugins {
    alias(libs.plugins.buildlogic.kmpexe)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":mahjong-utils"))
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
