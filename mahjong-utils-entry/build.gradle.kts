import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests

plugins {
    alias(libs.plugins.buildlogic.kmplib)
    alias(libs.plugins.buildlogic.mavenpublish)
    alias(libs.plugins.devPetsuka.npmPublish)
}

kotlin {
    js(IR) {
        browser {
            binaries.library()
            useCommonJs()
        }
        nodejs {
            binaries.library()
            useCommonJs()
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
    wasmJs {
        browser {
            binaries.library()
            useCommonJs()
        }
        nodejs {
            binaries.library()
            useCommonJs()
        }
        applyBinaryen()
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

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = targets.filterIsInstance<KotlinNativeTargetWithHostTests>().firstOrNull()
    nativeTarget?.binaries {
        sharedLib {
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
        val nativeMain by getting {
            dependsOn(nonJsMain)
        }
        val nativeTest by getting {
            dependsOn(nonJsTest)
        }
        val wasmJsMain by getting {
            dependsOn(nonJsMain)
        }
        val wasmJsTest by getting {
            dependsOn(nonJsTest)
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

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Mahjong Utils Entry")
            description.set("Provider a union entry for Mahjong Utils (for Japanese Riichi Mahjong)")
        }
    }
}
