package click.seichi.observerutils.contextualexecutor

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure

interface ContextualExecutor {
    fun executeWith(context: RawCommandContext): Result<Any, Throwable>

    fun tabCandidatesFor(context: RawCommandContext): List<String> = emptyList()

    fun asTabExecutor() = object: TabExecutor {
        override fun onTabComplete(
            sender: CommandSender,
            command: Command,
            alias: String,
            args: Array<out String>
        ): MutableList<String> {
            val context = RawCommandContext(sender, ExecutedCommand(command, alias), args.toList())

            return tabCandidatesFor(context).toMutableList()
        }

        override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
        ): Boolean {
            val context = RawCommandContext(sender, ExecutedCommand(command, label), args.toList())
            executeWith(context).onFailure {
                println("Error has occured while executing ${command.name} command.")
                it.printStackTrace()
            }

            return true
        }
    }
}
