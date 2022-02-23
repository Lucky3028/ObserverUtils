package click.seichi.observerutils

sealed interface Error

sealed interface CommandBuildException : Error {
    class FailedToCastSender(executor: String) : CommandBuildException {
        override val error = "このコマンドは${executor}のみが実行できます。"
    }

    class MissingArgument(missingArg: String) : CommandBuildException {
            override val error = "引数が足りません。: $missingArg"
    }

    val error: String
}
