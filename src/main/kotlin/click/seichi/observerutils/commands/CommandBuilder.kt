package click.seichi.observerutils.commands

import click.seichi.observerutils.CommandBuildException
import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.ResultOrErr
import click.seichi.observerutils.contextualexecutor.*
import click.seichi.observerutils.utils.splitFirst
import com.github.michaelbull.result.*
import org.bukkit.command.CommandSender

/**
 * The codes written in this file come from GiganticMinecraft/SeichiAssist which is licensed under GPLv3.
 * https://github.com/GiganticMinecraft/SeichiAssist/blob/develop/src/main/scala/com/github/unchama/contextualexecutor/builder/ContextualExecutorBuilder.scala
 */

data class CommandBuilder<CS : CommandSender>(
    var senderTypeValidation: SenderTypeValidation<CS>,
    var argumentsParser: CommandArgumentsParser<CS>,
    var contextualExecution: ScopedContextualExecution<CS>
) {
    companion object {
        private val defaultSenderValidation: SenderTypeValidation<CommandSender> = { Ok(it) }
        private val defaultArgumentsParser: CommandArgumentsParser<CommandSender> = { _, context ->
            Ok(PartiallyParsedArgs(emptyList(), context.args))
        }
        private val defaultExecution: ScopedContextualExecution<CommandSender> = { TODO("Not unimplemented!") }

        fun beginConfiguration(): CommandBuilder<CommandSender> =
            CommandBuilder(
                defaultSenderValidation,
                defaultArgumentsParser,
                defaultExecution
            )
    }

    fun argumentsParsers(parsers: List<SingleArgumentParser>): CommandBuilder<CS> {
        val combinedParser: CommandArgumentsParser<CS> = { _, context ->
            fun parse(
                remainingParsers: List<SingleArgumentParser>,
                remainingArgs: List<String>,
                reverseAccumulator: List<Any> = emptyList()
            ): ResultOrErr<PartiallyParsedArgs> {
                val (parserHead, parserTail) = remainingParsers.splitFirst() ?: run {
                    return Ok(PartiallyParsedArgs(reverseAccumulator.reversed(), remainingArgs))
                }

                val (argHead, argTail) = remainingArgs.splitFirst() ?: run {
                    return Err(CommandBuildException.MissingArgument())
                }

                return parserHead(argHead).flatMap { parsedArg ->
                    parse(parserTail, argTail, listOf(parsedArg) + reverseAccumulator)
                }
            }

            parse(parsers, context.args)
        }

        return copy(argumentsParser = combinedParser)
    }

    fun execution(execution: ScopedContextualExecution<CS>) = copy(contextualExecution = execution)

    inline fun <reified CS1 : CS> refineSender(senderType: String): CommandBuilder<CS1> {
        val newSenderTypeValidation: SenderTypeValidation<CS1> = { sender ->
            senderTypeValidation(sender).flatMap {
                runCatching { it as CS1 }.mapError { CommandBuildException.FailedToCastSender(senderType) }
            }
        }

        return CommandBuilder(newSenderTypeValidation, argumentsParser, contextualExecution)
    }

    fun build() = object : ContextualExecutor {
        override fun executeWith(context: RawCommandContext): EffectOrErr = binding {
            val refinedSender = senderTypeValidation(context.sender).bind()
            val parsedArgs = argumentsParser(refinedSender, context).bind()
            val parsedContext = ParsedArgCommandContext(refinedSender, context.command, parsedArgs)

            contextualExecution(parsedContext).bind()
        }
    }
}
