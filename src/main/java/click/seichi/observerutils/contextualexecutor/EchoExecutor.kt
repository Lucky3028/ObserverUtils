package click.seichi.observerutils.contextualexecutor

import arrow.core.Either

/**
 * [RawCommandContext.sender]に[messages]を送信するExecutor
 * @param messages 送信する文字列。
 */
class EchoExecutor(private vararg val messages: String) : ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext) =
        Either.Right(Effect.MessageEffect(*messages))
}
