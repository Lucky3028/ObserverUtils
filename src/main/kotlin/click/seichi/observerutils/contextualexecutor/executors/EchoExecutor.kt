package click.seichi.observerutils.contextualexecutor.executors

import arrow.core.Either
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.RawCommandContext

/**
 * [RawCommandContext.sender]に[messages]を送信するExecutor
 * @param messages 送信する文字列。
 */
class EchoExecutor(private vararg val messages: String) : ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext) =
        Either.Right(Effect.MessageEffect(*messages))
}
