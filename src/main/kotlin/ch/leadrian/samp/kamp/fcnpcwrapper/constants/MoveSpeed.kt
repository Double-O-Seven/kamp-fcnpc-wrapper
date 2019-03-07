package ch.leadrian.samp.kamp.fcnpcwrapper.constants

import ch.leadrian.samp.kamp.core.api.constants.ConstantValue
import ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants

enum class MoveSpeed(override val value: Float) : ConstantValue<Float> {
    AUTO(FCNPCConstants.FCNPC_MOVE_SPEED_AUTO),
    WALK(FCNPCConstants.FCNPC_MOVE_SPEED_WALK),
    RUN(FCNPCConstants.FCNPC_MOVE_SPEED_RUN),
    SPRINT(FCNPCConstants.FCNPC_MOVE_SPEED_SPRINT);

    companion object : ConstantValueRegistry<Float, MoveSpeed>(MoveSpeed.values())
}