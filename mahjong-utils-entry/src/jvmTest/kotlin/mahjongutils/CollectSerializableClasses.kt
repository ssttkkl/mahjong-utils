package mahjongutils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.reflections.Reflections
import java.io.File
import kotlin.test.Test


class CollectSerializableClasses {
    /**
     * 生成reflect-config.json供GraalVM编译时使用
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun collectSerializableClasses() {
        // https://github.com/Kotlin/kotlinx.serialization/issues/1125
        val reflections = Reflections("mahjongutils")
        val classes = reflections.getTypesAnnotatedWith(Serializable::class.java)

        val reflectConfig = buildJsonArray {
            classes.forEach { clazz ->
                println(clazz.canonicalName)

                add(buildJsonObject {
                    put("name", clazz.canonicalName)
                    put("fields", buildJsonArray {
                        add(buildJsonObject {
                            put("name", "Companion")
                        })
                    })
                })
                add(buildJsonObject {
                    put("name", clazz.canonicalName + "\$Companion")
                    put("methods", buildJsonArray {
                        add(buildJsonObject {
                            put("name", "serializer")
                            put("parameterTypes", buildJsonArray {
                                repeat(clazz.typeParameters.size) {
                                    add(KSerializer::class.qualifiedName)
                                }
                            })
                        })
                    })
                })
            }
        }

        val reflectConfigFile = File("reflect-config.json")
        reflectConfigFile.outputStream().use {
            Json.encodeToStream(reflectConfig, it)
        }
    }
}