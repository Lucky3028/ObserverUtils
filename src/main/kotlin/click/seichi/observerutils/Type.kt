package click.seichi.observerutils

import click.seichi.observerutils.contextualexecutor.Effect
import com.github.michaelbull.result.Result

typealias EffectOrThrowable = ResultOrThrowable<Effect>
typealias ResultOrThrowable<T> = Result<T, Throwable>

typealias EffectOrErr = ResultOrErr<Effect>
typealias ResultOrErr<T> = Result<T, Error>
