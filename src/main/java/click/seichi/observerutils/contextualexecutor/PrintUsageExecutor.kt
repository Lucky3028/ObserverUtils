package click.seichi.observerutils.contextualexecutor

import arrow.core.Either
import click.seichi.observerutils.EffectOrThrowable

object PrintUsageExecutor : ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext): EffectOrThrowable =
        Either.Right(Effect.MessageEffect(context.command.command.usage))
}
