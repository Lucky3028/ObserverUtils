package click.seichi.observerutils.utils

import arrow.core.None
import arrow.core.getOrElse
import arrow.core.toOption
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

object ExternalPlugin {
    object WorldGuard {
        private val instance = Bukkit.getPluginManager().getPlugin("WorldGuard").toOption()
            .map { it as WorldGuardPlugin }.getOrElse { throw Exception("WorldGuard cannot be found!") }

        private fun regionManager(world: World) = instance.regionContainer.get(world).toOption()

        fun getRegions(world: World, location: Location) =
            regionManager(world).map { it.getApplicableRegions(location).filterNotNull().toSet() }
                .flatMap { if (it.isEmpty()) None else it.toOption() }
    }
}
