package click.seichi.observerutils

import click.seichi.observerutils.ObserverUtils.Companion.PLUGIN

object Config {
    private val Config = PLUGIN.config

    val SERVER_NAME by lazy { Config.getString("serverName", "不明なサーバー")!! }
    val REDMINE_API_KEY by lazy { Config.getString("redmineApiKey") ?: throw Exception("Unset config: redmine") }
}
