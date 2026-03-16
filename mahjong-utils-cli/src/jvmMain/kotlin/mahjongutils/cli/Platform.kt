package mahjongutils.cli

actual fun getEnv(name: String): String? = System.getenv(name)
