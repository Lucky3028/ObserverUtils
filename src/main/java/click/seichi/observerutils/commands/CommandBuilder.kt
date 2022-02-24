package click.seichi.observerutils.commands

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.getOrElse
import click.seichi.observerutils.CommandBuildException
import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.ResultOrErr
import click.seichi.observerutils.contextualexecutor.*
import click.seichi.observerutils.utils.splitFirst
import org.bukkit.command.CommandSender

data class CommandBuilder<CS : CommandSender>(
    var senderTypeValidation: SenderTypeValidation<CS>,
    var argumentsParser: CommandArgumentsParser<CS>,
    var contextualExecution: ScopedContextualExecution<CS>
) {
    companion object {
        private val defaultSenderValidation: SenderTypeValidation<CommandSender> = { Either.Right(it) }
        private val defaultArgumentsParser: CommandArgumentsParser<CommandSender> = { _, context ->
            Either.Right(PartiallyParsedArgs(emptyList(), context.args))
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
                val (parserHead, parserTail) = remainingParsers.splitFirst().getOrElse {
                    return Either.Right(PartiallyParsedArgs(reverseAccumulator.reversed(), remainingArgs))
                }

                val (argHead, argTail) = remainingArgs.splitFirst().getOrElse {
                    return Either.Left(CommandBuildException.MissingArgument())
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
                Either.catch { it as CS1 }.mapLeft { CommandBuildException.FailedToCastSender(senderType) }
            }
        }

        return CommandBuilder(newSenderTypeValidation, argumentsParser, contextualExecution)
    }

    fun build() = object : ContextualExecutor {
        override suspend fun executeWith(context: RawCommandContext): EffectOrErr = either {
            val refinedSender = senderTypeValidation(context.sender).bind()
            val parsedArgs = argumentsParser(refinedSender, context).bind()
            val parsedContext = ParsedArgCommandContext(refinedSender, context.command, parsedArgs)

            contextualExecution(parsedContext).bind()
        }
    }
}
