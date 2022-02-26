package click.seichi.observerutils.contextualexecutor.executors

import arrow.core.Either
import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.RawCommandContext

object PrintUsageExecutor : ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext): EffectOrErr =
        Either.Right(Effect.MessageEffect(context.command.command.usage))
}
