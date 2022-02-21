package click.seichi.observerutils.contextualexecutor

import arrow.core.Either

object PrintUsageExecutor: ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext): Either<Throwable, Effect> =
        Either.Right(Effect.MessageEffect(context.command.command.usage))
}
