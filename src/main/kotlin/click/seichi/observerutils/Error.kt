package click.seichi.observerutils

/**
 * 本プラグインにおけるErrorを表現する基本interface
 */
sealed interface Error {
    val error: String
}

/**
 * [Exception]を[Error]でラップするためのclass
 */
class WrappedException(private val exception: Exception) : Error {
    /**
     * [exception]のスタックトレースを出力する
     */
    fun printStackTrace() = exception.printStackTrace()

    /**
     * [exception]のスタックトレースを[String]で返す
     */
    fun stackTrace() = exception.stackTraceToString()

    /**
     * [stackTrace]のシンタックスシュガー
     */
    override val error = stackTrace()
}

/**
 * [click.seichi.observerutils.commands.CommandBuilder]で使用されるべきコマンド実行時のエラーを表現する
 */
sealed interface CommandBuildException : Error {
    /**
     * [click.seichi.observerutils.commands.CommandBuilder.refineSender]で指定された型へのキャストが失敗したことを表現する
     * @param expectedSender 例外メッセージ中に本来はどの型でなくてはいけないのかということを挿入する。デフォルトは「(不明)」
     */
    class FailedToCastSender(expectedSender: String = "(不明)") : CommandBuildException {
        override val error = "このコマンドは$expectedSender のみが実行できます。"
    }

    /**
     * [click.seichi.observerutils.commands.CommandBuilder.argumentsParser]で引数をパースする際に、実行コマンドに引数が足りないことを表現する
     * @param messages 表示するメッセージ。
     */
    class MissingArgument(vararg messages: String) : CommandBuildException {
        override val error = messages.joinToString("\n")
    }

    /**
     * [click.seichi.observerutils.commands.CommandBuilder.argumentsParser]で引数をパースする際に、パーサーがエラーを返してきたことを表現する
     */
    class FailedToParseArg(override val error : String) : CommandBuildException
}

/**
 * HTTP通信の例外を[Error]にラップするためのinterface
 */
sealed interface HttpException : Error {
    val statusCode: Int
}

/**
 * 想定内の通信エラーを表現する
 * @param statusCode HttpStatusCode
 * @param error 例外メッセージ。
 */
enum class KnownHttpException(override val statusCode: Int, override val error: String) : HttpException {
    Unauthorized(401, "認証に失敗しました。APIキーを確認してください。"),
    Forbidden(403, "コンテンツへのアクセスが拒否されました。権限設定を確認してください。"),
    NotFound(404, "指定されたURLは存在しません。"),
    UnProcessableEntity(422, "エンティティを処理できませんでした。Bodyを確認してください。");
}

/**
 * 予期しない通信エラーを表現する
 * @param statusCode HttpStatusCode
 * @param error 例外メッセージ。通常は[okhttp3.Response.message]を指定する。
 */
data class UnknownHttpException(override val statusCode: Int, override val error: String) : HttpException

enum class WorldGuardException(override val error: String) : Error {
    SelectionIsNotFound("選択範囲がありません。"),
    Pos1IsNotFound("1st選択がありません。"),
    Pos2ndIsNotFound("2nd選択がありません。"),
    RegionIsNotFound("保護がありません。"),
}
