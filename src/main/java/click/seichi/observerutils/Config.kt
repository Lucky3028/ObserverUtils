package click.seichi.observerutils

import click.seichi.observerutils.ObserverUtils.Companion.PLUGIN
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration

/**
 * [Config]の設定項目をpathと一緒に保持するenum。設定を追加・変更・削除したら、このenumも編集すること。
 */
private enum class Configs(val path: String) {
    SERVER_NAME("server-name"), REDMINE_API_KEY("redmine-api-key")
}

/**
 * SpigotのConfig（[FileConfiguration]）をラップしたobject。
 */
object Config {
    private lateinit var Config: FileConfiguration
    private fun configDefinitions() = Configs.values().toSet().filterNot { Config.isSet(it.path) }.map { it.path }

    /**
     * プラグインが読み込まれ始めたら真っ先に読み込まれなくてはいけない[Config]の初期化関数。
     *
     * 設定がされていないときは例外をthrowし、本プラグインの読み込みを中止する。
     * @throws IllegalArgumentException 必要な設定が存在しない時。
     */
    fun init() {
        PLUGIN.saveDefaultConfig()
        Config = PLUGIN.config

        val configDefs = configDefinitions()
        if (configDefs.isNotEmpty()) {
            Bukkit.getPluginManager().disablePlugin(PLUGIN)
            throw IllegalArgumentException("必要な設定が存在しません。config.ymlを確認してください。: ${configDefs.joinToString()}")
        }
    }

    /**
     * サーバー名を表現する
     */
    val SERVER_NAME by lazy { Config.getString(Configs.SERVER_NAME.path)!! }

    /**
     * RedmineのAPIキーを表現する
     */
    val REDMINE_API_KEY by lazy { Config.getString(Configs.REDMINE_API_KEY.path)!! }
}
