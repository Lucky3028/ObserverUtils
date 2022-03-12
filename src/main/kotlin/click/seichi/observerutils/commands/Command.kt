package click.seichi.observerutils.commands

import click.seichi.observerutils.Config
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.asTabExecutor
import click.seichi.observerutils.contextualexecutor.executors.BranchedExecutor
import click.seichi.observerutils.contextualexecutor.executors.EchoExecutor
import click.seichi.observerutils.contextualexecutor.executors.TraverseExecutor
import click.seichi.observerutils.redmine.RedmineClient
import click.seichi.observerutils.redmine.RedmineIssue
import click.seichi.observerutils.redmine.RedmineTracker
import click.seichi.observerutils.utils.ExternalPlugin
import click.seichi.observerutils.utils.ExternalPlugin.WorldGuard
import click.seichi.observerutils.utils.formatted
import click.seichi.observerutils.utils.orEmpty
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.mapBoth
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * `/obs`コマンドを表現する。サブコマンドで成立している。
 */
object Command {
    fun executor() = BranchedExecutor(
        mapOf(
            "rg" to Commands.Region.executor,
            "fix" to Commands.Fix.executor,
            "help" to Commands.Help.executor
        ), Commands.Help.executor, Commands.Help.executor
    ).asTabExecutor()
}

object Commands {
    /**
     * Redmineに不要保護報告チケットを発行する。
     *
     * * プレイヤーのみ実行可能。
     * * プレイヤーの現在座標にWorldGuardの保護が1つ以上ない場合は実行不可。
     * * コメントは半角スペースで区切ると改行される。入力しなくてもよい。
     */
    object Region {
        private val usage = listOf("/obs rg <...コメント>", "　　Redmineに不要保護報告チケットを発行する").toTypedArray()

        val help = EchoExecutor(*usage)

        val executor =
            CommandBuilder.beginConfiguration().refineSender<Player>("Player").argumentsParsers(
                listOf(
                    Parsers.formattedDate(*dateFormatters, failureMessage = "lastquitが適切な形式で入力されていません。"),
                    Parsers.listedInt(Reason.Region.ids(), "理由が適切な形式で入力されていません。")
                ),
                onMissingArguments = usage
            ).execution { context ->
                val player = context.sender
                val regions = WorldGuard.getRegions(player.world, player.location)
                val topRegion = regions.firstOrNull() ?: run {
                    return@execution Ok(Effect.MessageEffect("${ChatColor.RED}保護がありません。"))
                }
                val duplicatedRegions =
                    if (regions.size >= 2) "(${regions.size}): ${regions.joinToString { it.id }}" else "-"
                val comment = context.args.yetToBeParsed.orEmpty("-") { it.joinToString("\n") }
                val description = """
                    |_.サーバー|${Config.SERVER_NAME}|
                    |_.ワールド|${player.world.name}|
                    |_.座標|/tp ${player.location.formatted()}|
                    |_.保護名|${topRegion.id}|
                    |_.保護Owner|${topRegion.owners.uniqueIds.filterNotNull().formatted()}|
                    |_.保護Member|${topRegion.members.uniqueIds.filterNotNull().formatted()}|
                    |_.重複保護|$duplicatedRegions|
                    |_.報告者コメント|$comment|
                """.trimIndent()
                val issue = RedmineIssue(
                    RedmineTracker.REGION,
                    "${RedmineTracker.REGION.jaName} (${Config.SERVER_NAME} ${player.world.name})",
                    description
                )
                val response = RedmineClient(Config.REDMINE_API_KEY).postIssue(issue)

                Ok(response.mapBoth(
                    success = { Effect.MessageEffect("${ChatColor.AQUA}Redmineにチケットを発行しました。") },
                    failure = {
                        Effect.SequentialEffect(
                            Effect.MessageEffect("${ChatColor.RED}Redmineにチケットを発行できませんでした。時間を空けて再度試すか、管理者に連絡してください。"),
                            Effect.LoggerEffect(
                                "Redmineにチケットを発行できませんでした。: ${it.first.statusCode}(${it.first.error})",
                                it.second
                            )
                        )
                    }
                ))
            }.build()
    }

    /**
     * Redmineに修繕依頼チケットを発行する。
     *
     * * プレイヤーのみ実行可能。
     * * プレイヤーがWorldEditで範囲を（pos1、pos2の両方）指定していない場合は実行不可。
     * * コメントは半角スペースで区切ると改行される。入力しなくてもよい。
     */
    object Fix {
        val help = EchoExecutor("/obs fix <...コメント>", "　　Redmineに修繕依頼チケットを発行する")

        val executor =
            CommandBuilder.beginConfiguration().refineSender<Player>("Player").execution { context ->
                val player = context.sender
                val selection = ExternalPlugin.WorldEdit.getSelections(player).getOrElse {
                    return@execution Ok(Effect.MessageEffect("${ChatColor.RED}範囲が選択されていません。"))
                }
                val comment = context.args.yetToBeParsed.orEmpty("-") { it.joinToString("\n") }
                val description = """
                    |_.サーバー|${Config.SERVER_NAME}|
                    |_.ワールド|${player.world.name}|
                    |_.座標|${selection.min.formatted()} -> ${selection.max.formatted()}|
                    |_.報告者コメント|$comment|
                """.trimIndent()
                val issue = RedmineIssue(
                    RedmineTracker.FIX,
                    "${RedmineTracker.FIX.jaName} (${Config.SERVER_NAME} ${player.world.name})",
                    description
                )
                val response = RedmineClient(Config.REDMINE_API_KEY).postIssue(issue)

                Ok(response.mapBoth(
                    success = { Effect.MessageEffect("${ChatColor.AQUA}Redmineにチケットを発行しました。") },
                    failure = {
                        Effect.SequentialEffect(
                            Effect.MessageEffect("${ChatColor.RED}Redmineにチケットを発行できませんでした。時間を空けて再度試すか、管理者に連絡してください。"),
                            Effect.LoggerEffect(
                                "Redmineにチケットを発行できませんでした。: ${it.first.statusCode}(${it.first.error})",
                                it.second
                            )
                        )
                    }
                ))
            }.build()
    }

    /**
     * コマンドの一覧と説明を表示する。
     */
    object Help {
        val executor = TraverseExecutor(Region.help, Fix.help, EchoExecutor("/obs help", "　　コマンドの一覧と説明を表示する"))
    }
}
