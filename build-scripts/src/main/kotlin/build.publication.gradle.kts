import java.util.*

plugins {
    `maven-publish`
    signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null
ext["gprUsername"] = null
ext["gprToken"] = null

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    ext["gprUsername"] = System.getenv("GPR_USERNAME")
    ext["gprToken"] = System.getenv("GPR_TOKEN")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            val url = if (version.toString().endsWith("SNAPSHOT"))
                "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            else
                "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            setUrl(url)
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ssttkkl/mahjong-utils")
            credentials {
                username = getExtraString("gprUsername")
                password = getExtraString("gprToken")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {

        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("Mahjong Utils")
            description.set("Mahjong Utils (for Japanese Riichi Mahjong)")
            url.set("https://github.com/ssttkkl/mahjong-utils")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("ssttkkl")
                    name.set("ssttkkl")
                    email.set("huang.wen.long@hotmail.com")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/ssttkkl/mahjong-utils.git")
                developerConnection.set("scm:git:ssh://github.com:ssttkkl/mahjong-utils.git")
                url.set("https://github.com/ssttkkl/mahjong-utils/tree/master")
            }

        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used

signing {
    sign(publishing.publications)
}