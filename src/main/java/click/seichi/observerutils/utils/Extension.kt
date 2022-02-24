package click.seichi.observerutils.utils

import arrow.core.Option
import org.bukkit.Location

fun Location.formatted() = "${this.blockX} ${this.blockY} ${this.blockZ}"

fun <T> Collection<T>.splitFirst() = Option.catch { this.first() to this.drop(1) }
fun <T> Collection<T>.orEmpty(default: String) = if (this.isEmpty()) default else this.toString()
fun <T> Collection<T>.orEmpty(default: String, formatter: (Collection<T>) -> String) = if (this.isEmpty()) default else formatter(this)
