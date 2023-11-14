package mahjongutils.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension


val Project.libs
    get() = rootProject.extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")
