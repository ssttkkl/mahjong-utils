import mahjongutils.buildlogic.enableJs
import mahjongutils.buildlogic.enableNative
import mahjongutils.buildlogic.enableWasm
import org.apache.commons.io.FileUtils
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.buildlogic.kmplib)
    alias(libs.plugins.buildlogic.mavenpublish)
    alias(libs.plugins.devPetsuka.npmPublish)
}

kotlin {
    if (enableJs) {
        js(IR) {
            browser {
                binaries.library()
            }
            nodejs {
                binaries.library()
            }
            useCommonJs()
        }
    }
    wasmJs {
        browser {
            binaries.library()
        }
        nodejs {
            binaries.library()
        }
        useCommonJs()
        compilations["main"].packageJson {
            name = "mahjong-utils-entry-wasm"
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
    wasmWasi {
        nodejs {
            binaries.library()
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.sharedLib {
            baseName = if (isMingwX64) "libmahjongutils" else "mahjongutils"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":mahjong-utils"))
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting
        val nonJsMain by creating {
            dependsOn(commonMain)
        }
        val nonJsTest by creating {
            dependsOn(commonTest)
        }
        val jvmMain by getting {
            dependsOn(nonJsMain)
        }
        val jvmTest by getting {
            dependsOn(nonJsTest)
        }
        if (enableNative) {
            val nativeMain by getting {
                dependsOn(nonJsMain)
            }
            val nativeTest by getting {
                dependsOn(nonJsTest)
            }
        }
        if (enableWasm) {
            val wasmMain by creating {
                dependsOn(commonMain)
            }
            val wasmTest by creating {
                dependsOn(commonTest)
            }
            val wasmJsMain by getting {
                dependsOn(wasmMain)
            }
            val wasmJsTest by getting {
                dependsOn(wasmTest)
            }
            val wasmWasiMain by getting {
                dependsOn(wasmMain)
            }
            val wasmWasiTest by getting {
                dependsOn(wasmTest)
            }
        }
    }
}

npmPublish {
    packages {
        if (enableJs) {
            get("js").apply {
                packageJson {
                    name = "mahjong-utils-entry"
                    author {
                        name = "ssttkkl"
                        email = "huang.wen.long@hotmail.com"
                    }
                    license = "MIT"
                }
            }
        }
        if (enableWasm) {
            get("wasmJs").apply {
                packageJson {
                    name = "mahjong-utils-entry-wasm"
                    author {
                        name = "ssttkkl"
                        email = "huang.wen.long@hotmail.com"
                    }
                    license = "MIT"
                }
            }
        }
    }
    registries {
        register("npmjs") {
            uri.set("https://registry.npmjs.org")
        }
        register("verdaccio") {
            uri.set("http://localhost:4873")
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Mahjong Utils Entry")
            description.set("Provider a union entry for Mahjong Utils (for Japanese Riichi Mahjong)")
        }
    }
}

if (enableNative) {
    afterEvaluate {
        val hostOs = System.getProperty("os.name")
        val isMingwX64 = hostOs.startsWith("Windows")
        val isAarch64 = System.getProperty("os.arch") == "aarch64"
        val currentOsTargetName = when {
            hostOs == "Mac OS X" -> {
                if (isAarch64) {
                    "macosArm64"
                } else {
                    "macosX64"
                }
            }

            hostOs == "Linux" -> {
                if (isAarch64) {
                    "linuxArm64"
                } else {
                    "linuxX64"
                }
            }

            isMingwX64 -> "mingwX64"
            else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
        }
        tasks.create("linkDebugSharedForCurrentOs").apply {
            val inputDir = buildDir.resolve("bin/${currentOsTargetName}/debugShared")
            val outputDir = buildDir.resolve("bin/currentOs/debugShared")

            dependsOn("linkDebugShared" + currentOsTargetName.capitalize())
            doLast {
                FileUtils.copyDirectory(inputDir, outputDir)
            }
        }
        tasks.create("linkReleaseSharedForCurrentOs").apply {
            val inputDir = buildDir.resolve("bin/${currentOsTargetName}/releaseShared")
            val outputDir = buildDir.resolve("bin/currentOs/releaseShared")

            dependsOn("linkReleaseShared" + currentOsTargetName.capitalize())
            doLast {
                FileUtils.copyDirectory(inputDir, outputDir)
            }
        }
    }
}