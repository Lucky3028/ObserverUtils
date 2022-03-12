package click.seichi.observerutils.redmine

enum class World(val worldName: String, val ja: String) {
    Main("world_2", "メイン"),
    Seichi1("world_SW", "第1整地"),
    Seichi2("world_SW_2", "第2整地"),
    Seichi3("world_SW_3", "第3整地"),
    Seichi4("world_SW_4", "第4整地"),
    SeichiNether("world_SW_nether", "ネザー整地"),
    SeichiEnd("world_SW_the_end", "エンド整地"),
    Build("world_build", "建築専用"),
    Dot("world_dot", "ドット絵");

    companion object {
        fun fromBukkitWorld(world: org.bukkit.World): World? = values().find { it.worldName == world.name }
    }
}
