package click.seichi.observerutils.utils

import click.seichi.observerutils.WorldGuardException
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.toResultOr
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
        private val instance = Bukkit.getPluginManager()?.getPlugin("WorldGuard")?.let { it as WorldGuardPlugin }

        private fun regionManager(world: World) = instance?.regionContainer?.get(world)

        /**
         * 指定された[world]の[location]に存在するWorldGuardの保護を返す
         * @param world 保護を取得したい[World]
         * @param location 保護を取得したい[Location]
         * @return 指定された[world]の[location]に存在するWorldGuardの保護の[Set]。保護がなければ`null`。
         */
        fun getRegions(world: World, location: Location) =
            regionManager(world)?.getApplicableRegions(location)?.filterNotNull()?.toSet().orEmpty()

        /**
         * 指定された[world]に存在する保護から指定された[regionName]の保護を探す
         * @param world 保護を取得したい[World]
         * @param regionName 取得したい保護の保護名
         * @return 指定された[regionName]のWorldGuard保護。存在しなければ`null`。
         */
        fun findRegionByName(world: World, regionName: String) = regionManager(world)?.getRegion(regionName)
            .toResultOr { WorldGuardException.RegionIsNotFound }
    }

    object WorldEdit {
        private val instance = Bukkit.getPluginManager()?.getPlugin("WorldEdit")?.let { it as WorldEditPlugin }

        /**
         * WorldEditの[com.sk89q.worldedit.bukkit.selections.Selection]をラップするdata class。
         */
        data class Selection(val min: Location, val max: Location)

        /**
         * 指定された[Player]がWorldEditで選択している範囲の最小座標と最大座標を[Location]で返す
         * @param p 選択範囲を取得したい[Player]
         * @return [p]がWorldEditで選択している範囲の最小座標と最大座標を示す[Location]を持つ[Selection]。両方選択されていなければ`null`。
         */
        fun getSelections(p: Player): Result<Selection, WorldGuardException> = binding {
            val selection = instance?.getSelection(p).toResultOr { WorldGuardException.SelectionIsNotFound }.bind()
            val min = selection.minimumPoint.toResultOr { WorldGuardException.Pos1IsNotFound }.bind()
            val max = selection.maximumPoint.toResultOr { WorldGuardException.Pos2ndIsNotFound }.bind()

            Selection(min, max)
        }
    }
}
