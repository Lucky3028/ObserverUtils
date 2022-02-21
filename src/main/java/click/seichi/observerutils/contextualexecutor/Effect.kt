package click.seichi.observerutils.contextualexecutor

import org.bukkit.command.CommandSender

sealed class Effect {
    object EmptyEffect : Effect()
    data class MessageEffect(private val messages: List<String>) : Effect() {
        constructor(message: String) : this(listOf(message))

        override fun run(sender: CommandSender) = sender.sendMessage(messages.toTypedArray())
    }

    open fun run(sender: CommandSender) {
        /** Nothing to do */
    }
}
