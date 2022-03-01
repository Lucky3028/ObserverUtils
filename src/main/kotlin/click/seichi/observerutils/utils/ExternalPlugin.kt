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
         * 指定された[World]の[Location]に存在するWorldGuardの保護を返す
         * @param world 保護を取得したい[World]
         * @param location 保護を取得したい[Location]
         * @return 指定された[World]の[Location]に存在するWorldGuardの保護の[Set]。保護がなければ`null`。
         */
        fun getRegions(world: World, location: Location) =
            regionManager(world)?.getApplicableRegions(location)?.filterNotNull()?.toSet().orEmpty()
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
         * @return 指定された[Player]がWorldEditで選択している範囲の最小座標と最大座標を示す[Location]を持つ[Selection]。両方選択されていなければ`null`。
         */
        fun getSelections(p: Player): Result<Selection, WorldGuardException> = binding {
            val selection = instance?.getSelection(p).toResultOr { WorldGuardException.SelectionIsNotFound }.bind()
            val min = selection.minimumPoint.toResultOr { WorldGuardException.Pos1IsNotFound }.bind()
            val max = selection.maximumPoint.toResultOr { WorldGuardException.Pos2ndIsNotFound }.bind()

            Selection(min, max)
        }
    }
}
