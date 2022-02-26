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

    fun log(level: LoggerLevel = INFO, vararg messages: String) = when (level) {
        INFO -> info(*messages)
        WARN -> warn(*messages)
        SEVERE -> severe(*messages)
    }

    fun info(vararg messages: String) = messages.forEach { logger.info(it) }
    fun warn(vararg messages: String) = messages.forEach { logger.warning(it) }
    fun severe(vararg messages: String) = messages.forEach { logger.severe(it) }
}