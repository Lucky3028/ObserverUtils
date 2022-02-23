package click.seichi.observerutils.contextualexecutor

import arrow.core.Either
import click.seichi.observerutils.EffectOrErr

object PrintUsageExecutor : ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext): EffectOrErr =
        Either.Right(Effect.MessageEffect(context.command.command.usage))
}
