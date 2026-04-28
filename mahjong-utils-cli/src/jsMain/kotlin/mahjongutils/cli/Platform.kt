package mahjongutils.cli

actual fun getEnv(name: String): String? = js("process.env[name]") as? String
