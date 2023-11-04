plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.jetbrains.dokka) apply false
    alias(libs.plugins.kotlinx.kover) apply false
    alias(libs.plugins.devPetsuka.npmPublish) apply false
}

group = property("group").toString()
version = property("version").toString()

tasks.wrapper {
    gradleVersion = "8.4"
    distributionType = Wrapper.DistributionType.ALL
}
