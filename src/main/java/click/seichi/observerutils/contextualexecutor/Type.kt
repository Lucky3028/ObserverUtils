package click.seichi.observerutils.contextualexecutor

import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.ResultOrErr
import org.bukkit.command.CommandSender

typealias SenderTypeValidation<CS> = suspend (CommandSender) -> ResultOrErr<CS>
typealias CommandArgumentsParser<CS> = (CS, RawCommandContext) -> ResultOrErr<PartiallyParsedArgs>
typealias ScopedContextualExecution<CS> = (ParsedArgCommandContext<CS>) -> EffectOrErr
typealias SingleArgumentParser = (String) -> ResultOrErr<Any>
