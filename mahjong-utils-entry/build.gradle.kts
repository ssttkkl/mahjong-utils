import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests

plugins {
    alias(libs.plugins.buildlogic.kmplib)
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

    val nativeTarget = targets.filterIsInstance<KotlinNativeTargetWithHostTests>().firstOrNull()
    nativeTarget?.binaries {
        sharedLib {
            baseName = if (nativeTarget.name == "mingwX64") "libmahjongutils" else "mahjongutils"
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
