package click.seichi.observerutils.redmine

/**
 * 整地鯖でのそれぞれのトラッカーを表現するenum。
 */
enum class RedmineTracker(val id: Int, val jaName: String) {
    REGION(36, "不要保護報告"), FIX(15, "修繕依頼")
}