package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.data.NodeInfo
import ch.leadrian.samp.kamp.fcnpcwrapper.data.PlayNodeParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import ch.leadrian.samp.kamp.fcnpcwrapper.exception.NodeNotOpenException
import ch.leadrian.samp.kamp.fcnpcwrapper.exception.OpenNodeFailedException

class Node
internal constructor(
        override val id: NodeId,
        private val nativeFunctions: FCNPCNativeFunctions
) : Entity<NodeId>, AutoCloseable {

    init {
        if (id.value !in 0 until FCNPCConstants.FCNPC_MAX_NODES) {
            throw IllegalArgumentException("Invalid node ID: ${id.value}")
        }
    }

    val isOpen: Boolean
        get() = nativeFunctions.isNodeOpen(id.value)

    val type: Int
        get() = requireOpen { nativeFunctions.getNodeType(id.value) }

    val pointCount: Int by lazy {
        nativeFunctions.getNodePointCount(id.value)
    }

    val pointCoordinates: Vector3D
        get() {
            val x = MutableFloatCell()
            val y = MutableFloatCell()
            val z = MutableFloatCell()
            nativeFunctions.getNodePointPosition(nodeid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }

    val info: NodeInfo
        get() {
            val numberOfVehicleNodes = MutableIntCell()
            val numberOfPedNodes = MutableIntCell()
            val numberOfNaviNodes = MutableIntCell()
            nativeFunctions.getNodeInfo(
                    nodeid = id.value,
                    vehnodes = numberOfVehicleNodes,
                    pednodes = numberOfPedNodes,
                    navinode = numberOfNaviNodes
            )
            return NodeInfo(
                    numberOfVehicleNodes = numberOfVehicleNodes.value,
                    numberOfPedNodes = numberOfPedNodes.value,
                    numberOfNaviNodes = numberOfNaviNodes.value
            )
        }

    fun open() {
        if (isOpen) {
            return
        }
        val success = nativeFunctions.openNode(id.value)
        if (!success) {
            throw OpenNodeFailedException("Failed to open node ${id.value}")
        }
    }

    override fun close() {
        nativeFunctions.closeNode(id.value)
    }

    fun setPoint(pointId: Int) {
        requireOpen {
            if (pointId !in 0 until pointCount) {
                throw IllegalArgumentException("point ID $pointId is not a valid point for node ${id.value} with $pointCount points")
            }
            nativeFunctions.setNodePoint(nodeid = id.value, pointid = pointId)
        }
    }

    @JvmOverloads
    fun play(npc: FullyControllableNPC, parameters: PlayNodeParameters = PlayNodeParameters.DEFAULT) {
        requireOpen {
            nativeFunctions.playNode(
                    npcid = npc.id.value,
                    nodeid = id.value,
                    move_type = parameters.moveType.value,
                    speed = parameters.speed.value,
                    mode = parameters.mode.value,
                    radius = parameters.radius,
                    set_angle = parameters.setAngle
            )
        }
    }

    inline fun <T> requireOpen(action: Node.() -> T): T {
        requireOpen()
        return action()
    }

    fun requireOpen() {
        if (!isOpen) {
            throw NodeNotOpenException("Node ${id.value} is not open")
        }
    }

}


