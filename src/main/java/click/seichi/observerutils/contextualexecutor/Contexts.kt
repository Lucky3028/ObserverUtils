package click.seichi.observerutils.contextualexecutor

import org.bukkit.command.CommandSender

data class ExecutedCommand(
    val command: org.bukkit.command.Command,
    val usedAlias: String
)

data class RawCommandContext(
    val sender: CommandSender,
    val command: ExecutedCommand,
    val args: List<String>
)
