package click.seichi.observerutils

import arrow.core.Option

fun <T> Collection<T>.splitFirst(): Option<Pair<T, List<T>>> = Option.catch { this.first() to this.drop(1) }
