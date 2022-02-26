package click.seichi.observerutils.utils

import arrow.core.None
import arrow.core.computations.option
import arrow.core.getOrElse
import arrow.core.toOption
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

/**
 * 外部プラグインを扱うためのobject。
 */
object ExternalPlugin {
    object WorldGuard {
        private val instance = Bukkit.getPluginManager().getPlugin("WorldGuard").toOption()
            .map { it as WorldGuardPlugin }.getOrElse { throw Exception("WorldGuard cannot be found!") }

        private fun regionManager(world: World) = instance.regionContainer.get(world).toOption()

        /**
         * 指定された[World]の[Location]に存在するWorldGuardの保護を返す
         * @param world 保護を取得したい[World]
         * @param location 保護を取得したい[Location]
         * @return 指定された[World]の[Location]に存在するWorldGuardの保護の[Set]。保護がなければ[None]。
         */
        fun getRegions(world: World, location: Location) =
            regionManager(world).map { it.getApplicableRegions(location).filterNotNull().toSet() }
                .flatMap { if (it.isEmpty()) None else it.toOption() }
    }

    object WorldEdit {
        private val instance = Bukkit.getPluginManager().getPlugin("WorldEdit").toOption()
            .map { it as WorldEditPlugin }.getOrElse { throw Exception("WorldEdit cannot be found!") }

        /**
         * WorldEditの[com.sk89q.worldedit.bukkit.selections.Selection]をラップするdata class。
         */
        data class Selection(val min: Location, val max: Location)

        /**
         * 指定された[Player]がWorldEditで選択している範囲の最小座標と最大座標を[Location]で返す
         * @param p 選択範囲を取得したい[Player]
         * @return 指定された[Player]がWorldEditで選択している範囲の最小座標と最大座標を示す[Location]を持つ[Selection]。両方選択されていなければ[None]。
         */
        suspend fun getSelections(p: Player) = option {
            val selection = instance.getSelection(p).toOption().bind()
            val min = selection.minimumPoint.toOption().bind()
            val max = selection.maximumPoint.toOption().bind()

            Selection(min, max)
        }
    }
}
