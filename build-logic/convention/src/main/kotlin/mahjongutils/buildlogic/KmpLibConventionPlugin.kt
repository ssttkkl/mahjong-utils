package mahjongutils.buildlogic

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KmpLibConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlin-multiplatform").get().get().pluginId)
            apply(libs.findPlugin("kotlin-serialization").get().get().pluginId)
            apply(libs.findPlugin("jetbrains-dokka").get().get().pluginId)
            apply(libs.findPlugin("kotlinx-kover").get().get().pluginId)
        }

        configure<KotlinMultiplatformExtension> {
            jvmToolchain(11)

            jvm {
                testRuns["test"].executionTask.configure {
                    useJUnitPlatform()
                    testLogging {
                        events("passed", "skipped", "failed")
                    }
                }
            }
            js(IR) {
                browser()
                nodejs()
            }

            val hostOs = System.getProperty("os.name")
            val isMingwX64 = hostOs.startsWith("Windows")
            val isAarch64 = System.getProperty("os.arch") == "aarch64"
            when {
                hostOs == "Mac OS X" -> {
                    if (isAarch64) {
                        macosArm64("native")
                    } else {
                        macosX64("native")
                    }
                }

                hostOs == "Linux" -> {
                    if (isAarch64) {
                        linuxArm64("native")
                    } else {
                        linuxX64("native")
                    }
                }

                isMingwX64 -> mingwX64("native")
                else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
            }

            sourceSets {
                val commonTest by getting {
                    dependencies {
                        implementation(kotlin("test"))
                    }
                }
            }
        }

        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
        }
    }
}