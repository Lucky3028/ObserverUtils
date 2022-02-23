package click.seichi.observerutils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class Tracker(val id: Int) {
    REGION(36), FIX(15)
}

const val IssueNewStatusId = 1
const val ObsProjectId = 4

@Serializable
class Issue(private val issue: RedmineIssue)

@Serializable
class RedmineIssue private constructor(
    @SerialName("project_id")
    val projectId: Int,
    @SerialName("tracker_id")
    val trackerId: Int,
    @SerialName("status_id")
    val statusId: Int,
    val subject: String,
    val description: String
) {
    constructor(
        tracker: Tracker, subject: String, description: String
    ) : this(ObsProjectId, tracker.id, IssueNewStatusId, subject, description)
}
