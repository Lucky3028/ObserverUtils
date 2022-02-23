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

data class PartiallyParsedArgs(
    val parsed: List<Any>,
    val yetToBeParsed: List<String>
)

data class ParsedArgCommandContext<out CS : CommandSender>(
    val sender: CS,
    val command: ExecutedCommand,
    val args: PartiallyParsedArgs
)
