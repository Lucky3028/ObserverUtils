package click.seichi.observerutils.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object Command : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender?,
        command: Command?,
        alias: String?,
        args: Array<out String>?
    ): MutableList<String> {
        return if (args?.size == 1) {
            SubCommands.values().map { it.name.lowercase() }.toMutableList()
        } else mutableListOf()
    }

    override fun onCommand(
        sender: CommandSender?,
        command: Command?,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        TODO("Not yet implemented")
    }
}