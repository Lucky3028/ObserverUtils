package click.seichi.observerutils.redmine

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.jsonBody
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RedmineClient(redmineApiKey: String) {
    private val redmineDomain = "https://redmine.seichi.click"
    private val redmineIssueUrl = "$redmineDomain/issues.json?key=$redmineApiKey"

    fun postIssue(issue: RedmineIssue): Pair<Request, Response> {
        val content = Json.encodeToString(Issue(issue))
        val res = Fuel.post(redmineIssueUrl).jsonBody(content).header("User-Agent", "curl/7.38.0").response()
        return res.first to res.second
    }
}