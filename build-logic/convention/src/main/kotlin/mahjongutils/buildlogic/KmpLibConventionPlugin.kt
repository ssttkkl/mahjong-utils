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
            applyDefaultHierarchyTemplate()
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
            wasmJs {
                browser()
                nodejs()
            }

            iosArm64()
            iosX64()
            iosSimulatorArm64()

            macosArm64()
            macosX64()
            linuxArm64()
            linuxX64()
            mingwX64()

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