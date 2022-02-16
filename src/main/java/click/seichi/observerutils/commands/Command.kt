package click.seichi.observerutils.commands

import click.seichi.observerutils.contextualexecutor.BranchedExecutor
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.RawCommandContext
import click.seichi.observerutils.contextualexecutor.asTabExecutor
import com.github.michaelbull.result.Result

object Command {
    fun executor() = BranchedExecutor(
        mapOf(
            "region" to Commands.REGION.executor(),
            "fix" to Commands.FIX.executor()
        ), Commands.HELP.executor(), Commands.HELP.executor()
    ).asTabExecutor()
}

enum class Commands {
    REGION {
        override fun executor(): ContextualExecutor = BranchedExecutor(
            mapOf(
                "fix" to FIX.executor(),
                "help" to HELP.executor()
            )
        )
    },
    FIX {
        override fun executor() = object: ContextualExecutor {
            override fun executeWith(context: RawCommandContext): Result<Any, Throwable> {
                TODO("Not yet implemented: fix")
            }
        }
    },
    HELP {
        override fun executor() = object: ContextualExecutor {
            override fun executeWith(context: RawCommandContext): Result<Any, Throwable> {
                TODO("Not yet implemented: help")
            }
        }
    };

    abstract fun executor(): ContextualExecutor
}
