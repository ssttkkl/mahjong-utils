plugins {
    alias(libs.plugins.buildlogic.kmplib)
    alias(libs.plugins.buildlogic.mavenpublish)
    alias(libs.plugins.kotest.multiplatform)
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
                implementation(kotlin("reflect"))
                implementation(platform("org.junit:junit-bom:5.10.0"))
                implementation("org.junit.jupiter:junit-jupiter")
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.framework.datatest)
                implementation(libs.kotlinx.serialization.json)
                implementation(project(":test-cases"))
            }
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Mahjong Utils")
            description.set("Mahjong Utils (for Japanese Riichi Mahjong)")
        }
    }
}
