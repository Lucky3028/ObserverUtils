package click.seichi.observerutils.commands

import click.seichi.observerutils.Config
import click.seichi.observerutils.contextualexecutor.Effect
import click.seichi.observerutils.contextualexecutor.Parsers
import click.seichi.observerutils.contextualexecutor.asTabExecutor
import click.seichi.observerutils.contextualexecutor.executors.BranchedExecutor
import click.seichi.observerutils.contextualexecutor.executors.EchoExecutor
import click.seichi.observerutils.contextualexecutor.executors.TraverseExecutor
import click.seichi.observerutils.redmine.*
import click.seichi.observerutils.utils.ExternalPlugin
import click.seichi.observerutils.utils.ExternalPlugin.WorldGuard
import click.seichi.observerutils.utils.MultipleType
import click.seichi.observerutils.utils.formatted
import click.seichi.observerutils.utils.orEmpty
import com.github.michaelbull.result.*
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * `/obs`コマンドを表現する。サブコマンドで成立している。
 */
object Command {
    fun executor() = BranchedExecutor(
        mapOf(
            "rg" to Commands.Region.executor,
            "fix" to Commands.Fix.executor,
            "tp" to Commands.Teleport.executor,
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
     * * 不要だと判断した理由が1つ以上指定されていないと実行不可。コンマで区切ると複数指定可能。
     */
    object Region {
        private val usage = listOf("/obs rg [判断理由の番号(コンマ区切り)] <...コメント>", "    Redmineに不要保護報告チケットを発行する").toTypedArray()

        val help = EchoExecutor(*usage)

        val executor =
            CommandBuilder.beginConfiguration().refineSender<Player>("Player").argumentsParsers(
                listOf(Parsers.listedInt(Reason.Region.ids(), "理由が適切な形式で入力されていません。")),
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
                    |_.保護名|@@${topRegion.id}@@|
                    |_.保護Owner|@@${topRegion.owners.uniqueIds.filterNotNull().formatted()}@@|
                    |_.保護Member|@@${topRegion.members.uniqueIds.filterNotNull().formatted()}@@|
                    |_.重複保護|@@$duplicatedRegions@@|
                    |_.報告者コメント|$comment|
                """.trimIndent()
                val world = World.fromBukkitWorld(player.world)?.ja ?: run {
                    return@execution Ok(Effect.MessageEffect("${ChatColor.RED}現在いるワールドでは不要保護報告は不要です。"))
                }
                val reasons = (context.args.parsed[0] as? List<*>)?.let { parsed ->
                    parsed.filterIsInstance<Int>().map { Reason.Region.values()[it].description }
                } ?: throw AssertionError()
                val issue = RedmineIssue(
                    RedmineTracker.REGION,
                    "${RedmineTracker.REGION.jaName} (${Config.SERVER_NAME} ${player.world.name})",
                    description,
                    listOf(
                        CustomField.Server to MultipleType(Config.SERVER_NAME),
                        CustomField.World to MultipleType(world),
                        CustomField.Location to MultipleType("/tp ${player.location.formatted()}"),
                        CustomField.Reason to MultipleType(values = reasons),
                        CustomField.ObserverId to MultipleType(player.name)
                    )
                )

                RedmineClient(Config.REDMINE_API_KEY).postIssue(issue)
                    .map { Effect.MessageEffect("${ChatColor.AQUA}Redmineにチケットを発行しました。") }
                    .recover { (err, json) ->
                        Effect.SequentialEffect(
                            Effect.MessageEffect("${ChatColor.RED}Redmineにチケットを発行できませんでした。時間を空けて再度試すか、管理者に連絡してください。"),
                            Effect.LoggerEffect(
                                "Redmineにチケットを発行できませんでした。: ${err.statusCode}(${err.error})",
                                json
                            )
                        )
                    }
            }.build()
    }

    /**
     * Redmineに修繕依頼チケットを発行する。
     *
     * * プレイヤーのみ実行可能。
     * * プレイヤーがWorldEditで範囲を（pos1、pos2の両方）指定していない場合は実行不可。
     * * コメントは半角スペースで区切ると改行される。入力しなくてもよい。
     * * 依頼の内容が1つ以上指定されていないと実行不可。コンマで区切ると複数指定可能。
     */
    object Fix {
        val help = EchoExecutor("/obs fix [修繕内容の番号(コンマ区切り)] <...コメント>", "    Redmineに修繕依頼チケットを発行する")

        val executor = CommandBuilder.beginConfiguration().refineSender<Player>("Player")
            .argumentsParsers(
                listOf(Parsers.listedInt(Reason.Fix.ids(), "修繕内容が適切な形式で入力されていません。"))
            ).execution { context ->
                val player = context.sender
                val selection = ExternalPlugin.WorldEdit.getSelections(player).getOrElse {
                    return@execution Ok(Effect.MessageEffect("${ChatColor.RED}範囲が選択されていません。"))
                }
                val comment = context.args.yetToBeParsed.orEmpty("-") { it.joinToString("\n") }
                val description = """
                    |_.報告者ID|${player.name}|
                    |_.報告者コメント|$comment|
                """.trimIndent()
                val world = World.fromBukkitWorld(player.world)?.ja ?: run {
                    return@execution Ok(Effect.MessageEffect("${ChatColor.RED}現在いるワールドでは修繕依頼はできません。"))
                }
                val contents = (context.args.parsed[0] as? List<*>)?.let { parsed ->
                    parsed.filterIsInstance<Int>().map { Reason.Fix.values()[it].description }
                } ?: throw AssertionError()
                val issue = RedmineIssue(
                    RedmineTracker.FIX,
                    "${RedmineTracker.FIX.jaName} (${Config.SERVER_NAME} ${player.world.name})",
                    description,
                    listOf(
                        CustomField.Server to MultipleType(Config.SERVER_NAME),
                        CustomField.World to MultipleType(world),
                        CustomField.Location to MultipleType("/tp ${selection.min.formatted()}"),
                        CustomField.Location2 to MultipleType("/tp ${selection.max.formatted()}"),
                        CustomField.Content to MultipleType(values = contents)
                    )
                )

                RedmineClient(Config.REDMINE_API_KEY).postIssue(issue)
                    .map { Effect.MessageEffect("${ChatColor.AQUA}Redmineにチケットを発行しました。") }
                    .recover { (err, json) ->
                        Effect.SequentialEffect(
                            Effect.MessageEffect("${ChatColor.RED}Redmineにチケットを発行できませんでした。時間を空けて再度試すか、管理者に連絡してください。"),
                            Effect.LoggerEffect(
                                "Redmineにチケットを発行できませんでした。: ${err.statusCode}(${err.error})",
                                json
                            )
                        )
                    }
            }.build()
    }

    object Teleport {
        val help = EchoExecutor("/obs tp [保護名]", "    指定した保護にテレポートする")

        val executor = CommandBuilder.beginConfiguration().refineSender<Player>("Player")
            .argumentsParsers(listOf(Parsers.singleString("保護名が指定されていません。")))
            .execution { context ->
                val sender = context.sender

                (context.args.parsed[0] as String)
                    .let { WorldGuard.findRegionByName(sender.world, it) }
                    .map { it.maximumPoint!! }
                    .map { Location(sender.world, it.x, 64.0, it.z) }
                    .onSuccess { sender.teleport(it) }
                    .map {
                        Effect.MessageEffect(
                            "${ChatColor.AQUA}保護が見つかったためテレポートしました。",
                            "${ChatColor.AQUA}座標: ${it.x} ${it.y} ${it.z}"
                        )
                    }
            }.build()
    }

    /**
     * コマンドの一覧と説明を表示する。
     */
    object Help {
        val executor = TraverseExecutor(Region.help, Fix.help, Teleport.help,
            EchoExecutor("/obs help", "    コマンドの一覧と説明を表示する"))
    }
}
