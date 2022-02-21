package click.seichi.observerutils.contextualexecutor

import arrow.core.Either
import click.seichi.observerutils.Effect

object PrintUsageExecutor: ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext): Either<Throwable, Effect> =
        Either.Right(Effect.MessageEffect(context.command.command.usage))
}
