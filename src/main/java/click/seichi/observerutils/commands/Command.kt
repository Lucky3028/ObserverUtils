package click.seichi.observerutils.commands

import arrow.core.Either
import arrow.core.getOrElse
import click.seichi.observerutils.Config
import click.seichi.observerutils.contextualexecutor.BranchedExecutor
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.asTabExecutor
import click.seichi.observerutils.redmine.RedmineClient
import click.seichi.observerutils.redmine.RedmineIssue
import click.seichi.observerutils.redmine.Tracker
import click.seichi.observerutils.utils.ExternalPlugin.WorldGuard
import click.seichi.observerutils.utils.formatted
import click.seichi.observerutils.utils.orEmpty
import org.bukkit.entity.Player

object Command {
    fun executor() = BranchedExecutor(
        mapOf(
            "region" to Commands.REGION.executor(),
            "fix" to Commands.FIX.executor()
        ), Commands.HELP.executor(), Commands.HELP.executor()
    ).asTabExecutor()
}

private fun Collection<UUID>.formatted() =
    this.orEmpty("-") { it.map { uuid -> Bukkit.getOfflinePlayer(uuid).name ?: uuid }.joinToString() }

enum class Commands {
    REGION {
        override fun executor() =
            CommandBuilder.beginConfiguration().refineSender<Player>("Player").execution { context ->
                val player = context.sender
                val regions = WorldGuard.getRegions(player.world, player.location).getOrElse {
                    return@execution Either.Right(Effect.MessageEffect("${ChatColor.RED}保護がありません。"))
                }
                val isSomeRegions = regions.size >= 2
                val topRegion = regions.first()
                // TODO: /tp location
                val description = """
                    |_.サーバー|${Config.SERVER_NAME}|
                    |_.ワールド|${player.world}|
                    |_.座標|${player.location.formatted()}|
                    |_.保護名|${topRegion.id}|
                    |_.保護Owner|${topRegion.owners.players.orEmpty("-")}|
                    |_.保護Member|${topRegion.members.players.orEmpty("-")}|
                    |_.重複保護|${if (isSomeRegions) regions.size else "-"}|
                    |_.報告者コメント|${context.args.yetToBeParsed.firstOrNull() ?: "-"}|
                """.trimIndent()
                val issue = RedmineIssue(Tracker.REGION, "不要保護報告(${Config.SERVER_NAME} ${player.world})", description)
                val res = RedmineClient(Config.REDMINE_API_KEY).postIssue(issue)
                // TOOD: 返答を見やすく
                Either.Right(Effect.LoggerEffect(res.first.toString() + "\n" + res.second.statusCode))
            }.build()
    },
    FIX {
        override fun executor() = CommandBuilder.beginConfiguration().execution {
            Either.Right(Effect.MessageEffect(it.command.command.name, it.args.toString()))
        }.build()
    },
    HELP {
        override fun executor() = CommandBuilder.beginConfiguration().execution {
            Either.Right(Effect.MessageEffect("ObserverUtils Help"))
        }.build()
    };

    abstract fun executor(): ContextualExecutor
}
