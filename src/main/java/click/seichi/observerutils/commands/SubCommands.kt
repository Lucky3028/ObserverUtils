package click.seichi.observerutils.commands

enum class SubCommands(val alias: Set<String> = emptySet(), val description: String) {
    REGION(setOf("rg"), "不要保護報告を行う"),
    FIX(description = "修繕報告を行う")
}