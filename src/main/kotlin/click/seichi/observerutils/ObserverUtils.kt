package click.seichi.observerutils

import click.seichi.observerutils.commands.Command
import org.bukkit.plugin.java.JavaPlugin

class ObserverUtils : JavaPlugin() {
    companion object {
        lateinit var PLUGIN: ObserverUtils
            private set
    }

    override fun onEnable() {
        PLUGIN = this
        getCommand("obs").executor = Command.executor()

        Config.init()
    }
}