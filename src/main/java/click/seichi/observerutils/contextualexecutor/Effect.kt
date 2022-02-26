package click.seichi.observerutils.contextualexecutor

import click.seichi.observerutils.utils.Logger
import click.seichi.observerutils.utils.LoggerLevel
import org.bukkit.command.CommandSender

sealed interface Effect {
    object EmptyEffect : Effect

    class MessageEffect(private vararg val messages: String) : Effect {
        override fun run(sender: CommandSender) = sender.sendMessage(messages)
    }

    class LoggerEffect(
        private vararg val messages: String,
        private val level: LoggerLevel = LoggerLevel.INFO,
    ) : Effect {
        override fun run(sender: CommandSender) = Logger.log(level, *messages)
    }

    class SequentialEffect(private vararg val effects: Effect) : Effect {
        override fun run(sender: CommandSender) = effects.forEach { it.run(sender) }
    }

    fun run(sender: CommandSender) {
        /** Nothing to do */
    }
}
