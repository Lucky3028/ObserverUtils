package click.seichi.observerutils.contextualexecutor

import arrow.core.Either
import click.seichi.observerutils.EffectOrError

object PrintUsageExecutor : ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext): EffectOrError =
        Either.Right(Effect.MessageEffect(context.command.command.usage))
}
