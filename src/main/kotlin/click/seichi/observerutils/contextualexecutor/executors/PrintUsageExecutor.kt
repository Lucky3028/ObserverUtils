package click.seichi.observerutils.contextualexecutor.executors

import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.RawCommandContext
import com.github.michaelbull.result.Ok

object PrintUsageExecutor : ContextualExecutor {
    override fun executeWith(context: RawCommandContext): EffectOrErr =
        Ok(Effect.MessageEffect(context.command.command.usage))
}
