package click.seichi.observerutils

import arrow.core.Either
import click.seichi.observerutils.contextualexecutor.Effect

typealias EffectOrError = ResultOrError<Effect>
typealias ResultOrError<T> = Either<Throwable, T>
