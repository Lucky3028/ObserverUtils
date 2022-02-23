package click.seichi.observerutils.contextualexecutor

import click.seichi.observerutils.EffectOrError
import click.seichi.observerutils.ResultOrError
import org.bukkit.command.CommandSender

typealias SenderTypeValidation<CS> = (CommandSender) -> ResultOrError<CS>
typealias CommandArgumentsParser<CS> = (CS, RawCommandContext) -> ResultOrError<PartiallyParsedArgs>
typealias ScopedContextualExecution<CS> = (ParsedArgCommandContext<CS>) -> EffectOrError
typealias SingleArgumentParser = (String) -> ResultOrError<Any>
