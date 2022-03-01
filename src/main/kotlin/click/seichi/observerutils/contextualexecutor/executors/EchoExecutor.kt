package click.seichi.observerutils.contextualexecutor.executors

import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.RawCommandContext
import com.github.michaelbull.result.Ok

/**
 * [RawCommandContext.sender]に[messages]を送信するExecutor
 * @param messages 送信する文字列。
 */
class EchoExecutor(private vararg val messages: String) : ContextualExecutor {
    override fun executeWith(context: RawCommandContext) = Ok(Effect.MessageEffect(*messages))
}
