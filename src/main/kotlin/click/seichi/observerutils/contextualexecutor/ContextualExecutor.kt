package click.seichi.observerutils.contextualexecutor

import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.WrappedException
import com.github.shynixn.mccoroutine.SuspendingCommandExecutor
import com.github.shynixn.mccoroutine.SuspendingTabCompleter
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

interface ContextualExecutor {
    suspend fun executeWith(context: RawCommandContext): EffectOrErr

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
            executeWith(context).fold(
                ifLeft = {
                    if (it is WrappedException) {
                        Effect.MessageEffect("${ChatColor.RED}不明なエラーが発生しました。管理者に連絡してください。").run(context.sender)
                        it.printStackTrace()
                    } else {
                        Effect.MessageEffect(it.error).run(context.sender)
                    }
                },
                ifRight = { it.run(context.sender) }
            )

            return true
        }
    }