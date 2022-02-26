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
    class FailedToCastSender(expectedSender: String = "(不明)") : CommandBuildException {
        override val error = "予期しない実行者がコマンドを実行しました。$expectedSender のみが実行できます。"
    }

    class MissingArgument(missingArg: String = "(不明)") : CommandBuildException {
        override val error = "引数が足りません。: $missingArg"
    }
}

sealed interface HttpException : Error {
    val statusCode: Int
}

enum class KnownHttpException(override val statusCode: Int, override val error: String) : HttpException {
    Unauthorized(401, "認証に失敗しました。APIキーを確認してください。"),
    Forbidden(403, "コンテンツへのアクセスが拒否されました。権限設定を確認してください。"),
    NotFound(404, "指定されたURLは存在しません。"),
    UnProcessableEntity(422, "エンティティを処理できませんでした。Bodyを確認してください。");
}

data class UnknownHttpException(override val statusCode: Int, override val error: String) : HttpException
