plugins {
    kotlin("multiplatform") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
    id("build.publication")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        // To build distributions for and run tests on browser or Node.js use one or both of:
        browser()
        nodejs()
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test")) // This brings all the platform dependencies automatically
            }
        }
    }
}

tasks.wrapper {
    gradleVersion = "7.4.2"
    distributionType = Wrapper.DistributionType.ALL
}
