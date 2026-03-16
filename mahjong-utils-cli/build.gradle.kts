plugins {
    alias(libs.plugins.buildlogic.kmpexe)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
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
