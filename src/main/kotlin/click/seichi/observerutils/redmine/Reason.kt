package click.seichi.observerutils.redmine

object Reason {
    enum class Region(val description: String) {
        OwnersLastQuit("未建築または建築途中で、全Ownerのlastquitが7日以上前"),
        OwnersPermanentBan("全Ownerが永久BANを受けている"),
        Duplicated("同一箇所に異常なほど重なっている"),
        OnlyOneBlock("1マスのみである"),
        Elongated("極端に長方形である"),
        TooUnused("活用済みの土地が著しく少ない"),
        Other("その他");

        companion object {
            fun ids() = values().map { it.ordinal }
        }
    }

    enum class Fix(val description: String) {
        FloatingBlocks("空中ブロック"),
        Magma("マグマ放置"),
        Water("水放置"),
        Tunnel("トンネル状"),
        Other("その他");

        companion object {
            fun ids() = Region.values().map { it.ordinal }
        }
    }
}
