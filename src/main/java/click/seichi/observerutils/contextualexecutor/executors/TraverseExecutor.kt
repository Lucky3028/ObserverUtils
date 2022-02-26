package click.seichi.observerutils.contextualexecutor.executors

import arrow.core.Either
import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.RawCommandContext

/**
 * [executors]をすべて実行するExecutor
 * @param executors 実行する[ContextualExecutor]。
 */
class TraverseExecutor(private vararg val executors: ContextualExecutor): ContextualExecutor {
    override suspend fun executeWith(context: RawCommandContext): EffectOrErr {
        executors.forEach { it.executeWith(context) }

        return Either.Right(Effect.EmptyEffect)
    }
}