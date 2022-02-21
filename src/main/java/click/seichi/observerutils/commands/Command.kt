package click.seichi.observerutils.commands

import arrow.core.Either
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.BranchedExecutor
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.asTabExecutor
import org.bukkit.Bukkit

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
        override fun executor() = CommandBuilder.beginConfiguration().execution {
            Either.Right(Effect.MessageEffect(listOf(it.command.command.name, it.args.toString())))
        }.build()
    },
    HELP {
        override fun executor() = CommandBuilder.beginConfiguration().execution {
            Bukkit.getServer().logger.info("help!")

            Either.Right(Effect.EmptyEffect)
        }.build()
    };

    abstract fun executor(): ContextualExecutor
}
