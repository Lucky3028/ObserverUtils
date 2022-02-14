package click.seichi.observerutils

import org.bukkit.plugin.java.JavaPlugin

class ObserverUtils : JavaPlugin() {
    override fun onEnable() {
        getCommand("obs").executor = Command
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}