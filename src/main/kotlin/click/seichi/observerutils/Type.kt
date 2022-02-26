package click.seichi.observerutils

import arrow.core.Either
import click.seichi.observerutils.contextualexecutor.Effect

typealias EffectOrThrowable = ResultOrThrowable<Effect>
typealias ResultOrThrowable<T> = Either<Throwable, T>

typealias EffectOrErr = ResultOrErr<Effect>
typealias ResultOrErr<T> = Either<Error, T>
