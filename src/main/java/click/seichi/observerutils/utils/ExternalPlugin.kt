package click.seichi.observerutils.utils

import arrow.core.None
import arrow.core.Option
import arrow.core.computations.option
import arrow.core.getOrElse
import arrow.core.toOption
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

object ExternalPlugin {
    object WorldGuard {
        private val instance = Bukkit.getPluginManager().getPlugin("WorldGuard").toOption()
            .map { it as WorldGuardPlugin }.getOrElse { throw Exception("WorldGuard cannot be found!") }

        private fun regionManager(world: World) = instance.regionContainer.get(world).toOption()

        fun getRegions(world: World, location: Location) =
            regionManager(world).map { it.getApplicableRegions(location).filterNotNull().toSet() }
                .flatMap { if (it.isEmpty()) None else it.toOption() }
    }

    object WorldEdit {
        private val instance = Bukkit.getPluginManager().getPlugin("WorldEdit").toOption()
            .map { it as WorldEditPlugin }.getOrElse { throw Exception("WorldEdit cannot be found!") }

        data class Selection(val min: Location, val max: Location)

        suspend fun getSelections(p: Player): Option<Selection> = option {
            val selection = instance.getSelection(p).toOption().bind()
            val min = selection.minimumPoint.toOption().bind()
            val max = selection.maximumPoint.toOption().bind()

            Selection(min, max)
        }
    }
}
