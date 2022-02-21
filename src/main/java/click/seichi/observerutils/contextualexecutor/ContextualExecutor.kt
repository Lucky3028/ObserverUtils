package click.seichi.observerutils.contextualexecutor

import arrow.core.Either
import com.github.shynixn.mccoroutine.SuspendingCommandExecutor
import com.github.shynixn.mccoroutine.SuspendingTabCompleter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

interface ContextualExecutor {
    suspend fun executeWith(context: RawCommandContext): Either<Throwable, Effect>

    fun tabCandidatesFor(context: RawCommandContext): List<String> = emptyList()
}

fun ContextualExecutor.asTabExecutor(): SuspendingCommandExecutor =
    object : SuspendingCommandExecutor, SuspendingTabCompleter {
        override suspend fun onTabComplete(
            sender: CommandSender,
            command: Command,
            alias: String,
            args: Array<out String>
        ): MutableList<String> {
            val context = RawCommandContext(sender, ExecutedCommand(command, alias), args.toList())

            return tabCandidatesFor(context).toMutableList()
        }

        override suspend fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
        ): Boolean {
            val context = RawCommandContext(sender, ExecutedCommand(command, label), args.toList())
            executeWith(context).tap { it.run(context.sender) }.tapLeft {
                println("Error has occured while executing ${command.name} command.")
                it.printStackTrace()
            }

            return true
        }
    }