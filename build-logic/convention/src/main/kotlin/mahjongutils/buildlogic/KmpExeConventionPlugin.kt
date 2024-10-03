package mahjongutils.buildlogic

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile


class KmpExeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(KmpLibConventionPlugin::class.java)
        }
        configure<KotlinMultiplatformExtension> {
            jvm {
                mainRun {
                    mainClass.set("MainKt")
                }
            }

            if (enableNative) {
                targets.withType<KotlinNativeTarget>().configureEach {
                    binaries.executable()
                }
            }
            if (enableJs) {
                js(IR) {
                    browser {
                        binaries.executable()
                    }
                    nodejs {
                        binaries.executable()
                    }
                    useCommonJs()
                }
            }
            if (enableWasm) {
                wasmJs {
                    browser {
                        binaries.executable()
                    }
                    nodejs {
                        binaries.executable()
                    }
                    useCommonJs()
                    compilations["main"].packageJson {
                        name = "mahjong-utils-benchmark-wasm"
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
                        binaries.executable()
                    }
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
                tasks.create("runDebugExecutableCurrentOs").apply {
                    group = "run"
                    dependsOn("runDebugExecutable" + currentOsTargetName.capitalize())
                }
                tasks.create("runReleaseExecutableCurrentOs").apply {
                    group = "run"
                    dependsOn("runReleaseExecutable" + currentOsTargetName.capitalize())
                }
            }
        }
    }
}