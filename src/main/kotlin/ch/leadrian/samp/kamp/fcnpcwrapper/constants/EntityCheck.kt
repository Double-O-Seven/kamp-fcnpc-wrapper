package ch.leadrian.samp.kamp.fcnpcwrapper.constants

import ch.leadrian.samp.kamp.core.api.constants.ConstantValue
import ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants

enum class EntityCheck(override val value: Int) : ConstantValue<Int> {
    NONE(FCNPCConstants.FCNPC_ENTITY_CHECK_NONE),
    PLAYER(FCNPCConstants.FCNPC_ENTITY_CHECK_PLAYER),
    NPC(FCNPCConstants.FCNPC_ENTITY_CHECK_NPC),
    ACTOR(FCNPCConstants.FCNPC_ENTITY_CHECK_ACTOR),
    VEHICLE(FCNPCConstants.FCNPC_ENTITY_CHECK_VEHICLE),
    OBJECT(FCNPCConstants.FCNPC_ENTITY_CHECK_OBJECT),
    PLAYER_OBJECT_ORIGIN(FCNPCConstants.FCNPC_ENTITY_CHECK_POBJECT_ORIG),
    PLAYER_OBJECT_TARGET(FCNPCConstants.FCNPC_ENTITY_CHECK_POBJECT_TARG),
    MAP(FCNPCConstants.FCNPC_ENTITY_CHECK_MAP),
    ALL(FCNPCConstants.FCNPC_ENTITY_CHECK_ALL);

    companion object : ConstantValueRegistry<Int, EntityCheck>(EntityCheck.values())
}