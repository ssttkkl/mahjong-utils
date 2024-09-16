package mahjongutils.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

val enableNative = System.getenv("enable_native")
    ?.equals("true", ignoreCase = true) != false

val enableJs = System.getenv("enable_js")
    ?.equals("true", ignoreCase = true) != false

val enableWasm = System.getenv("enable_wasm")
    ?.equals("true", ignoreCase = true) != false


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

            jvm {
                testRuns["test"].executionTask.configure {
                    useJUnitPlatform()
                    testLogging {
                        events("passed", "skipped", "failed")
                    }
                }
            }
            println("${project.name} target: jvm")

            if (enableJs) {
                js(IR) {
                    browser()
                    nodejs()
                }
                println("${project.name} target: js")
            }
            if (enableWasm) {
                @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
                wasmJs {
                    browser()
                    nodejs()
                }
                println("${project.name} target: wasmJs")
            }

            if (enableNative) {
                iosArm64()
                println("${project.name} target: iosArm64")
                iosX64()
                println("${project.name} target: iosX64")
                iosSimulatorArm64()
                println("${project.name} target: iosSimulatorArm64")
                macosArm64()
                println("${project.name} target: macosArm64")
                macosX64()
                println("${project.name} target: macosX64")
                linuxArm64()
                println("${project.name} target: linuxArm64")
                linuxX64()
                println("${project.name} target: linuxX64")
                mingwX64()
                println("${project.name} target: mingwX64")
            }

            sourceSets {
                val commonTest by getting {
                    dependencies {
                        implementation(kotlin("test"))
                    }
                }
            }
        }

        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
    }
}