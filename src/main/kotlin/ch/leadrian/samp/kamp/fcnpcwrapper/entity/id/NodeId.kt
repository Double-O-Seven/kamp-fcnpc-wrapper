package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import ch.leadrian.samp.kamp.core.api.entity.id.EntityId
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants

data class NodeId internal constructor(override val value: Int) : EntityId {

    companion object {

        private val nodeIds: Array<NodeId> = (0 until FCNPCConstants.MAX_NODES).map {
            NodeId(it)
        }.toTypedArray()

        fun valueOf(value: Int): NodeId =
                when {
                    0 <= value && value < nodeIds.size -> nodeIds[value]
                    else -> NodeId(value)
                }
    }
}