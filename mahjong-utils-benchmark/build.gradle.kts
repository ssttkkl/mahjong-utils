plugins {
    alias(libs.plugins.buildlogic.kmpexe)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":mahjong-utils"))
                implementation(kotlin("test"))
                implementation(libs.kotlinx.serialization.core)
            }
        }
    }
}
