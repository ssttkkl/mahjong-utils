plugins {
    kotlin("multiplatform") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
    id("dev.petuska.npm.publish") version "3.2.0"
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            useCommonJs()
            binaries.library()
        }
        compilations["main"].packageJson {
            name = "mahjong-utils-entry"
            customField(
                "author", mapOf(
                    "name" to "ssttkkl",
                    "email" to "huang.wen.long@hotmail.com"
                )
            )
            customField(
                "license", "MIT"
            )
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.binaries {
        sharedLib {
            baseName = if (isMingwX64) "libmahjongutils" else "mahjongutils"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(rootProject)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            }
        }
    }
}

npmPublish {
    registries {
        register("npmjs") {
            uri.set("https://registry.npmjs.org")
        }
        register("verdaccio") {
            uri.set("http://localhost:4873")
        }
    }
}