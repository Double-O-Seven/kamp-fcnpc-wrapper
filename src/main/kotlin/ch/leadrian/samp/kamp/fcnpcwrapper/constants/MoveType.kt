package ch.leadrian.samp.kamp.fcnpcwrapper.constants

import ch.leadrian.samp.kamp.core.api.constants.ConstantValue
import ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants

enum class MoveType(override val value: Int) : ConstantValue<Int> {
    AUTO(FCNPCConstants.MOVE_TYPE_AUTO),
    WALK(FCNPCConstants.MOVE_TYPE_WALK),
    RUN(FCNPCConstants.MOVE_TYPE_RUN),
    SPRINT(FCNPCConstants.MOVE_TYPE_SPRINT),
    DRIVE(FCNPCConstants.MOVE_TYPE_DRIVE);

    companion object : ConstantValueRegistry<Int, MoveType>(MoveType.values())
}