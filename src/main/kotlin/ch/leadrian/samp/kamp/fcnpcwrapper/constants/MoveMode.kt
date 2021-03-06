package ch.leadrian.samp.kamp.fcnpcwrapper.constants

import ch.leadrian.samp.kamp.core.api.constants.ConstantValue
import ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants

enum class MoveMode(override val value: Int) : ConstantValue<Int> {
    AUTO(FCNPCConstants.MOVE_MODE_AUTO),
    NONE(FCNPCConstants.MOVE_MODE_NONE),
    MAP_ANDREAS(FCNPCConstants.MOVE_MODE_MAPANDREAS),
    COL_ANDREAS(FCNPCConstants.MOVE_MODE_COLANDREAS);

    companion object : ConstantValueRegistry<Int, MoveMode>(MoveMode.values())
}