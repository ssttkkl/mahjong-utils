plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    id("application")
    alias(libs.plugins.graalvm.buildtools.native)
}

application {
    mainClass.set("mahjongutils.MainKt")
}

dependencies {
    implementation(project(":mahjong-utils"))
    implementation(project(":mahjong-utils-entry"))
    implementation(libs.kotlinx.serialization.json)
}


graalvmNative {
    binaries {
        named("main") {
            fallback.set(false)
            verbose.set(true)

            buildArgs.apply {
                add("-H:+UnlockExperimentalVMOptions")
                add("-H:-CheckToolchain")
                add("-H:+TraceNativeToolUsage")

                add("-H:+InstallExitHandlers")
                add("-H:+ReportUnsupportedElementsAtRuntime")
                add("-H:+ReportExceptionStackTraces")
            }
        }
    }
}