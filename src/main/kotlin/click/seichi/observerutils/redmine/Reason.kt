package click.seichi.observerutils.redmine

enum class Region(val description: String) {
    OwnersLastQuit(""),
    OwnersPermanentBan(""),
    Duplicated(""),
    OnlyOneBlock(""),
    Elongated(""),
    TooUnused(""),
    Unfinished(""),
    Other("その他")
}

enum class Fix(val description: String) {
    FloatingBlocks(""),
    Magma(""),
    Water(""),
    Tunnel(""),
    Other("その他")
}
