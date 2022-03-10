package click.seichi.observerutils.utils

data class MultipleType<T> (val value: T? = null, val values: List<T>? = null) {
    init { require(value != null || values != null) { "Both of values must not be empty!" } }

    val isMultiple = values != null
}