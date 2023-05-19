package click.seichi.observerutils

import click.seichi.observerutils.commands.Command
import okhttp3.OkHttpClient
import org.bukkit.plugin.java.JavaPlugin

class ObserverUtils : JavaPlugin() {
    companion object {
        lateinit var PLUGIN: ObserverUtils
            private set
        lateinit var HttpClient: OkHttpClient
            private set
    }

    override fun onEnable() {
        PLUGIN = this
        getCommand("obs")?.setExecutor(Command.executor())

        Config.init()
        HttpClient = OkHttpClient()
    }
}