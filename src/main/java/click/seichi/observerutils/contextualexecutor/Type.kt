package click.seichi.observerutils.contextualexecutor

import click.seichi.observerutils.EffectOrThrowable
import click.seichi.observerutils.ResultOrThrowable
import org.bukkit.command.CommandSender

typealias SenderTypeValidation<CS> = suspend (CommandSender) -> ResultOrThrowable<CS>
typealias CommandArgumentsParser<CS> = (CS, RawCommandContext) -> ResultOrThrowable<PartiallyParsedArgs>
typealias ScopedContextualExecution<CS> = (ParsedArgCommandContext<CS>) -> EffectOrThrowable
typealias SingleArgumentParser = (String) -> ResultOrThrowable<Any>
