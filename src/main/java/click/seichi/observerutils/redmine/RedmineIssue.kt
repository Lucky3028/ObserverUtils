package click.seichi.observerutils.redmine

const val IssueNewStatusId = 1
const val ObsProjectId = 4

class Issue(private val issue: RedmineIssue)

class RedmineIssue private constructor(
    val projectd: Int,
    val tracker_id: Int,
    val status_id: Int,
    val subject: String,
    val description: String
) {
    constructor(
        tracker: RedmineTracker, subject: String, description: String
    ) : this(ObsProjectId, tracker.id, IssueNewStatusId, subject, description)
}
