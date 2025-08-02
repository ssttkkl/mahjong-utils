plugins {
    alias(libs.plugins.buildlogic.kmplib)
    alias(libs.plugins.buildlogic.mavenpublish)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
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

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Mahjong Utils Model")
            description.set("Model classes for Mahjong Utils (for Japanese Riichi Mahjong)")
        }
    }
}

