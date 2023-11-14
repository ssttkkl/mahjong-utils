package mahjongutils.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension
import java.util.*
import kotlin.io.path.Path

class MavenPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.gradle.signing")
            apply("org.gradle.maven-publish")
        }

        // Stub secrets to let the project sync and build without the publication values set up
        extra["signing.keyId"] = null
        extra["signing.password"] = null
        extra["signing.secretKeyRingFile"] = null
        extra["ossrhUsername"] = null
        extra["ossrhPassword"] = null
        extra["gprUsername"] = null
        extra["gprToken"] = null

        // Grabbing secrets from local.properties file or from environment variables, which could be used on CI
        val secretPropsFile = rootProject.file("local.properties")
        if (secretPropsFile.exists()) {
            secretPropsFile.reader().use {
                Properties().apply { load(it) }
            }.onEach { (name, value) ->
                extra[name.toString()] = value
            }
        } else {
            extra["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
            extra["signing.password"] = System.getenv("SIGNING_PASSWORD")
            extra["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
            extra["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
            extra["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
            extra["gprUsername"] = System.getenv("GPR_USERNAME")
            extra["gprToken"] = System.getenv("GPR_TOKEN")
        }

        val secretKeyRingFile = Path(extra["signing.secretKeyRingFile"].toString())
        extra["signing.secretKeyRingFile"] = rootProject.file(secretKeyRingFile)

        val javadocJar by tasks.registering(Jar::class) {
            archiveClassifier.set("javadoc")
        }

        fun getExtraString(name: String) = extra[name]?.toString()

        extensions.configure<PublishingExtension> {
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
        extensions.configure<SigningExtension> {
            sign(extensions.getByType<PublishingExtension>().publications)
        }

        //region Fix Gradle warning about signing tasks using publishing task outputs without explicit dependencies
        // https://github.com/gradle/gradle/issues/26091
        tasks.withType<AbstractPublishToMaven>().configureEach {
            val signingTasks = tasks.withType<Sign>()
            mustRunAfter(signingTasks)
        }
    }
}