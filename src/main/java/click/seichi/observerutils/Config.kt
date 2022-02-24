package click.seichi.observerutils

import click.seichi.observerutils.ObserverUtils.Companion.PLUGIN
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration

enum class Configs(val path: String) {
    SERVER_NAME("server-name"), REDMINE_API_KEY("redmine-api-key")
}

object Config {
    private lateinit var Config: FileConfiguration
    private fun configDefinitions() = Configs.values().toSet().filterNot { Config.isSet(it.path) }.map { it.path }

    fun init() {
        PLUGIN.saveDefaultConfig()
        Config = PLUGIN.config

        val configDefs = configDefinitions()
        if (configDefs.isNotEmpty()) {
            Bukkit.getPluginManager().disablePlugin(PLUGIN)
            throw IllegalArgumentException("必要な設定が存在しません。config.ymlを確認してください。: ${configDefs.joinToString()}")
        }
    }

    val SERVER_NAME by lazy { Config.getString(Configs.SERVER_NAME.path)!! }
    val REDMINE_API_KEY by lazy { Config.getString(Configs.REDMINE_API_KEY.path)!! }
}
