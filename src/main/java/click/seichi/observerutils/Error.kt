package click.seichi.observerutils

sealed interface Error {
    val error: String
}

class WrappedException(private val exception: Exception) : Error {
    fun printStackTrace() = exception.printStackTrace()
    fun stackTrace() = exception.stackTraceToString()

    override val error = stackTrace()
}

sealed interface CommandBuildException : Error {
    class FailedToCastSender(executor: String = "(不明)") : CommandBuildException {
        override val error = "予期しない実行者がコマンドを実行しました。：$executor"
    }

    class MissingArgument(missingArg: String = "(不明)") : CommandBuildException {
        override val error = "引数が足りません。: $missingArg"
    }
}
