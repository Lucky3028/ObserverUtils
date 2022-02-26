package click.seichi.observerutils.redmine

/**
 * 「新規」ステータスを表現するマジックナンバー。
 */
const val IssueNewStatusId = 1

/**
 * 整地鯖におけるObserverのプロジェクトを表現するマジックナンバー。
 */
const val ObsProjectId = 4

/**
 * RedmineにIssueの内容として[RedmineIssue]を送る際に、以下のような形になっている必要があるため、[RedmineIssue]をラップするためのクラス。
 *
 * ```json
 * {
 *   issue: {
 *     project_id: 1,
 *     tracker_id: 1,
 *     status_id: 1,
 *     ...
 *  }
 * }
 * ```
 */
class Issue(private val issue: RedmineIssue)

/**
 * RedmineのIssueを表現する。
 *
 * シリアライズに使用するため、一部のフィールドは命名規則を無視した名前になっている。
 * （annotationがproguardで消されてしまうので、[com.google.gson.annotations.SerializedName]は使えない）
 */
class RedmineIssue private constructor(
    val project_id: Int,
    val tracker_id: Int,
    val status_id: Int,
    val subject: String,
    val description: String
) {
    constructor(
        tracker: RedmineTracker, subject: String, description: String
    ) : this(ObsProjectId, tracker.id, IssueNewStatusId, subject, description)
}
