package click.seichi.observerutils.utils

import click.seichi.observerutils.utils.LoggerLevel.*
import org.bukkit.Bukkit

/**
 * [java.util.logging.Level]のうち一般的に有効であろうと思われるロギングレベルを表現する
 */
enum class LoggerLevel {
    INFO, WARN, SEVERE
}

/**
 * [org.bukkit.Server.getLogger]をラップしている
 */
object Logger {
    private val logger = Bukkit.getServer().logger

    /**
     * [LoggerLevel]で[messages]をサーバーログに出力する
     * @param level ロギングレベル。デフォルトは[LoggerLevel.INFO]。
     * param messages 出力したいメッセージ。
     */
    fun log(level: LoggerLevel = INFO, vararg messages: String) = when (level) {
        INFO -> info(*messages)
        WARN -> warn(*messages)
        SEVERE -> severe(*messages)
    }

    /**
     * [log]を[LoggerLevel.INFO]で実行するシンタックスシュガー。
     */
    fun info(vararg messages: String) = messages.forEach { logger.info(it) }

    /**
     * [log]を[LoggerLevel.WARN]で実行するシンタックスシュガー。
     */
    fun warn(vararg messages: String) = messages.forEach { logger.warning(it) }

    /**
     * [log]を[LoggerLevel.SEVERE]で実行するシンタックスシュガー。
     */
    fun severe(vararg messages: String) = messages.forEach { logger.severe(it) }
}
