package click.seichi.observerutils.contextualexecutor

import arrow.core.Either
import click.seichi.observerutils.EffectOrErr

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