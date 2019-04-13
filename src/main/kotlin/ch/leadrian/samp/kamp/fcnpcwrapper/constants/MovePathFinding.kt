package ch.leadrian.samp.kamp.fcnpcwrapper.constants

import ch.leadrian.samp.kamp.core.api.constants.ConstantValue
import ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants

enum class MovePathFinding(override val value: Int) : ConstantValue<Int> {
    AUTO(FCNPCConstants.MOVE_PATHFINDING_AUTO),
    NONE(FCNPCConstants.MOVE_PATHFINDING_NONE),
    Z(FCNPCConstants.MOVE_PATHFINDING_Z),
    RAY_CAST(FCNPCConstants.MOVE_PATHFINDING_RAYCAST);

    companion object : ConstantValueRegistry<Int, MovePathFinding>(MovePathFinding.values())
}