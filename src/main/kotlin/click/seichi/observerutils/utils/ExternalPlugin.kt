package click.seichi.observerutils.utils

import click.seichi.observerutils.WorldGuardException
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.toResultOr
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldguard.bukkit.BukkitRegionContainer
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
        private val instance = Bukkit.getPluginManager().getPlugin("WorldGuard").let { it as WorldGuardPlugin }

        private fun regionManager(world: World) = BukkitRegionContainer(instance).get(BukkitWorld(world))

        /**
         * 指定された[world]の[location]に存在するWorldGuardの保護を返す
         * @param world 保護を取得したい[World]
         * @param location 保護を取得したい[Location]
         * @return 指定された[world]の[location]に存在するWorldGuardの保護の[Set]。保護がなければ`null`。
         */
        fun getRegions(world: World, location: Location) =
            regionManager(world)?.getApplicableRegions(BlockVector3.at(location.x, location.y, location.z))
                ?.filterNotNull()?.toSet().orEmpty()

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
        private val instance = Bukkit.getPluginManager().getPlugin("WorldEdit")?.let { it as WorldEditPlugin }!!

        /**
         * WorldEditの[org.bukkit.Location]をラップするdata class。
         */
        data class Selection(val min: Location, val max: Location)

        fun from(world: World, vec: BlockVector3) = Location(
            world, vec.blockX.toDouble(), vec.blockY.toDouble(), vec.blockZ.toDouble()
        )

        /**
         * 指定された[Player]がWorldEditで選択している範囲の最小座標と最大座標を[BlockVector3]で返す
         * @param player 選択範囲を取得したい[Player]
         * @return [player]がWorldEditで選択している範囲の最小座標と最大座標を示す[BlockVector3]を持つ[Selection]。両方選択されていなければ`null`。
         */
        fun getSelections(player: Player): Result<Selection, WorldGuardException> = binding {
            val actor = BukkitAdapter.adapt(player)
            val session = instance.worldEdit.sessionManager.get(actor)
            val world = session.selectionWorld.toResultOr { WorldGuardException.SelectionIsNotFound }.bind()
            val selection = session.getSelection(world)
            val min = selection.minimumPoint.toResultOr { WorldGuardException.Pos1IsNotFound }.bind()
            val max = selection.maximumPoint.toResultOr { WorldGuardException.Pos2ndIsNotFound }.bind()

            Selection(from(player.world, min), from(player.world, max))
        }
    }
}
