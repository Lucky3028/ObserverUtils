package click.seichi.observerutils.contextualexecutor

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

object PrintUsageExecutor: ContextualExecutor {
    override fun executeWith(context: RawCommandContext): Result<Any, Throwable> =
        Ok(context.sender.sendMessage(context.command.command.usage))
}
