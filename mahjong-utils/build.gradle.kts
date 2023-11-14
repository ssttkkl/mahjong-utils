plugins {
    alias(libs.plugins.buildlogic.kmplib)
    alias(libs.plugins.buildlogic.mavenpublish)
    alias(libs.plugins.jetbrains.dokka)
}


kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(platform("org.junit:junit-bom:5.10.0"))
                implementation("org.junit.jupiter:junit-jupiter")
            }
        }
    }
}
