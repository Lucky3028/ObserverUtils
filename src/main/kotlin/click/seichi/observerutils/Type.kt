package click.seichi.observerutils

import com.github.michaelbull.result.Result
import click.seichi.observerutils.contextualexecutor.Effect

typealias EffectOrThrowable = ResultOrThrowable<Effect>
typealias ResultOrThrowable<T> = Result<T, Throwable>

typealias EffectOrErr = ResultOrErr<Effect>
typealias ResultOrErr<T> = Result<T, Error>
