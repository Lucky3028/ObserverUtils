package click.seichi.observerutils.contextualexecutor

import click.seichi.observerutils.Logger
import click.seichi.observerutils.LoggerLevel
import org.bukkit.command.CommandSender

sealed class Effect {
    object EmptyEffect : Effect()
    data class MessageEffect(private val messages: List<String>) : Effect() {
        constructor(message: String) : this(listOf(message))

        override fun run(sender: CommandSender) = sender.sendMessage(messages.toTypedArray())
    }

    data class LoggerEffect(
        private val messages: List<String>,
        private val level: LoggerLevel = LoggerLevel.INFO,
    ) : Effect {
        constructor(message: String, level: LoggerLevel = LoggerLevel.INFO) : this(listOf(message), level)

        override fun run(sender: CommandSender) = Logger.log(messages, level)
    }

    data class SequantialEffect(private val effects: List<Effect>) : Effect {
        override fun run(sender: CommandSender) = effects.forEach { it.run(sender) }
    }

    open fun run(sender: CommandSender) {
        /** Nothing to do */
    }
}
