package click.seichi.observerutils.redmine

import click.seichi.observerutils.HttpException
import click.seichi.observerutils.KnownHttpException
import click.seichi.observerutils.ObserverUtils.Companion.HttpClient
import click.seichi.observerutils.UnknownHttpException
import click.seichi.observerutils.utils.MultipleType
import click.seichi.observerutils.utils.MultipleTypeAdapter
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

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
        val gson = GsonBuilder().registerTypeAdapter(MultipleType::class.java, MultipleTypeAdapter()).create()
        val json = gson.toJson(Issue(issue))
        val request = Request.Builder().url(redmineIssueUrl).post(json.toRequestBody("application/json".toMediaType()))
            .addHeader("User-Agent", "curl/7.38.0").build()
        val res = HttpClient.newCall(request).execute().use { it }
        return if (res.code in 200..399) Ok(res)
        else {
            val err = KnownHttpException.values().find { it.statusCode == res.code }
                ?: UnknownHttpException(res.code, res.message)
            Err(err to json)
        }
    }
}