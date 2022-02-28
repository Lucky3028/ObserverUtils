package click.seichi.observerutils.redmine

import click.seichi.observerutils.HttpException
import click.seichi.observerutils.KnownHttpException
import click.seichi.observerutils.UnknownHttpException
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.gson.Gson
import com.github.michaelbull.result.Result

/**
 * Redmineと通信を行うためのクラス。
 * @param redmineApiKey RedmineのAPIキー。
 */
class RedmineClient(redmineApiKey: String) {
    private val redmineDomain = "https://redmine.seichi.click"
    private val redmineIssueUrl = "$redmineDomain/issues.json?key=$redmineApiKey"

    /**
     * RedmineにIssueを作成する。
     * @param issue 作成するissueの内容。[RedmineIssue]を指定する。
     */
    fun postIssue(issue: RedmineIssue): Result<Response, Pair<HttpException, String>> {
        val content = Gson().toJson(Issue(issue))
        val res = Fuel.post(redmineIssueUrl).jsonBody(content).header("User-Agent", "curl/7.38.0").response()
        val statusCode = res.second.statusCode
        return if (statusCode in 200..399) Ok(res.second)
        else {
            val err = KnownHttpException.values().find { it.statusCode == statusCode }
                ?: UnknownHttpException(statusCode, res.second.responseMessage)
            Err(err to content)
        }
    }
}