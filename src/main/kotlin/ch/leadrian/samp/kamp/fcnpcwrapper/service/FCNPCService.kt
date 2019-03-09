package ch.leadrian.samp.kamp.fcnpcwrapper.service

import ch.leadrian.samp.kamp.core.api.amx.OutputString
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MovePathFinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCNPCService
@Inject
internal constructor(
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

}