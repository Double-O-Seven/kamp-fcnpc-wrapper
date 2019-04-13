package ch.leadrian.samp.kamp.fcnpcwrapper.service

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.amx.OutputString
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MovePathFinding
import ch.leadrian.samp.kamp.fcnpcwrapper.data.WeaponInfo
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePath
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.PlaybackRecord
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.FullyControllableNPCFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.MovePathFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.PlaybackRecordFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCNPCService
@Inject
internal constructor(
        private val fullyControllableNPCFactory: FullyControllableNPCFactory,
        private val nodeLoader: NodeLoader,
        private val movePathFactory: MovePathFactory,
        private val playbackRecordFactory: PlaybackRecordFactory,
        private val nativeFunctions: FCNPCNativeFunctions
) {

    var updateRate: Int
        get() = nativeFunctions.getUpdateRate()
        set(value) {
            nativeFunctions.setUpdateRate(value)
        }

    var tickRate: Int
        get() = nativeFunctions.getTickRate()
        set(value) {
            nativeFunctions.setTickRate(value)
        }

    var isCrashLogUsed: Boolean
        get() = nativeFunctions.isCrashLogUsed()
        set(value) {
            nativeFunctions.useCrashLog(value)
        }

    @JvmOverloads
    fun getPluginVersion(size: Int = 64): String {
        val version = OutputString(size)
        nativeFunctions.getPluginVersion(version, size)
        return version.value
    }

    @JvmOverloads
    fun useMoveMode(mode: MoveMode, use: Boolean = true) {
        nativeFunctions.useMoveMode(mode.value, use)
    }

    fun isMoveModeUsed(mode: MoveMode): Boolean = nativeFunctions.isMoveModeUsed(mode.value)

    @JvmOverloads
    fun useMovePathFinding(mode: MovePathFinding, use: Boolean = true) {
        nativeFunctions.useMovePathfinding(mode.value, use)
    }

    fun isMovePathFindingUsed(mode: MovePathFinding): Boolean = nativeFunctions.isMovePathfindingUsed(mode.value)

    fun setDefaultWeaponInfo(weapon: WeaponModel, info: WeaponInfo) {
        nativeFunctions.setWeaponDefaultInfo(
                weaponid = weapon.value,
                reload_time = info.reloadTime ?: -1,
                shoot_time = info.shootTime ?: -1,
                clip_size = info.clipSize ?: -1,
                accuracy = info.accuracy
        )
    }

    fun getDefaultWeaponInfo(weapon: WeaponModel): WeaponInfo {
        val reloadTime = MutableIntCell()
        val shootTime = MutableIntCell()
        val clipSize = MutableIntCell()
        val accuracy = MutableFloatCell()
        nativeFunctions.getWeaponDefaultInfo(
                weaponid = weapon.value,
                reload_time = reloadTime,
                shoot_time = shootTime,
                clip_size = clipSize,
                accuracy = accuracy
        )
        return WeaponInfo(
                reloadTime = reloadTime.value,
                shootTime = shootTime.value,
                clipSize = clipSize.value,
                accuracy = accuracy.value
        )
    }

    fun loadPlaybackRecord(file: String): PlaybackRecord = playbackRecordFactory.load(file)

    fun createNPC(name: String): FullyControllableNPC = fullyControllableNPCFactory.create(name)

    fun loadNode(nodeId: NodeId): Node = nodeLoader.load(nodeId)

    fun createMovePath(): MovePath = movePathFactory.create()

}