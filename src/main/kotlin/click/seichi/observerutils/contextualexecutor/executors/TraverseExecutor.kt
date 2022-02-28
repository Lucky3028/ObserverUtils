package click.seichi.observerutils.contextualexecutor.executors

import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.WrappedException
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.RawCommandContext
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.getOrElse
import org.bukkit.ChatColor

/**
 * [executors]をすべて実行するExecutor
 * @param executors 実行する[ContextualExecutor]。
 */
class TraverseExecutor(private vararg val executors: ContextualExecutor) : ContextualExecutor {
    override fun executeWith(context: RawCommandContext): EffectOrErr {
        val effects = executors.map {
            it.executeWith(context).getOrElse { err ->
                if (err is WrappedException) {
                    Effect.SequentialEffect(
                        Effect.MessageEffect("${ChatColor.RED}不明なエラーが発生しました。管理者に連絡してください。"),
                        Effect.LoggerEffect(err.stackTrace())
                    )
                } else {
                    Effect.MessageEffect(err.error)
                }
            }
        }

        return Ok(Effect.SequentialEffect(*effects.toTypedArray()))
    }
}