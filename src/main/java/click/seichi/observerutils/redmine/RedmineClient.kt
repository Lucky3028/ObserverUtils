package click.seichi.observerutils.redmine

import arrow.core.Either
import click.seichi.observerutils.HttpException
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.jsonBody
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import values

class RedmineClient(redmineApiKey: String) {
    private val redmineDomain = "https://redmine.seichi.click"
    private val redmineIssueUrl = "$redmineDomain/issues.json?key=$redmineApiKey"

    fun postIssue(issue: RedmineIssue): Either<HttpException, Response> {
        val content = Json.encodeToString(Issue(issue))
        val res = Fuel.post(redmineIssueUrl).jsonBody(content).header("User-Agent", "curl/7.38.0").response()
        val statusCode = res.second.statusCode
        return if (statusCode in 200..399) Either.Right(res.second)
        else Either.Left(
            HttpException.values().find { it.statusCode == statusCode } ?: HttpException.Unknown(statusCode, res.second.responseMessage)
        )
    }
}