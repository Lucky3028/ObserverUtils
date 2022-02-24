package click.seichi.observerutils

import SealedClassEnumExtension

sealed interface Error {
    val error: String
}

class WrappedException(val exception: Exception) : Error {
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

abstract class HttpException : Error {
    companion object: SealedClassEnumExtension<HttpException>

    object Unauthorized : HttpException() {
        override val statusCode = 401
        override val error = "認証に失敗しました。APIキーを確認してください。"
    }

    object Forbidden : HttpException() {
        override val statusCode = 403
        override val error = "コンテンツへのアクセスが拒否されました。権限設定を確認してください。"
    }

    object NotFound : HttpException() {
        override val statusCode = 404
        override val error = "指定されたURLは存在しません。"
    }

    object UnProcessableEntity : HttpException() {
        override val statusCode = 422
        override val error = "エンティティを処理できませんでした。Bodyを確認してください。"
    }

    class Unknown(override val statusCode: Int, override val error: String) : HttpException()

    abstract val statusCode: Int
}
