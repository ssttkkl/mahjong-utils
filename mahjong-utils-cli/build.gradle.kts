import mahjongutils.buildlogic.enableNative
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.buildlogic.kmplib)
    alias(libs.plugins.kotlin.serialization)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvm {
        withJava()
    }

    if (enableNative) {
        macosArm64()
        macosX64()
        linuxX64()
        linuxArm64()
        mingwX64()
        
        targets.withType<KotlinNativeTarget>().configureEach {
            binaries {
                executable {
                    entryPoint = "mahjongutils.cli.main"
                    baseName = "mahjong-utils-cli"
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":mahjong-utils"))
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}

tasks.register<Jar>("executableJar") {
    archiveClassifier.set("executable")
    from(kotlin.jvm().compilations.getByName("main").output)
    from(configurations.getByName("jvmRuntimeClasspath").map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "mahjongutils.cli.MainKt"
    }
}
