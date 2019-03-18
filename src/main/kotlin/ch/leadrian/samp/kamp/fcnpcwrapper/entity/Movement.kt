package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.data.GoToParameters

class Movement
internal constructor(
        override val npc: FullyControllableNPC,
        private val nativeFunctions: FCNPCNativeFunctions
) : HasFullyControllableNPC {

    val isMoving: Boolean
        get() = nativeFunctions.isMoving(npc.id.value)

    val destinationPoint: Vector3D
        get() {
            val x = MutableFloatCell()
            val y = MutableFloatCell()
            val z = MutableFloatCell()
            nativeFunctions.getDestinationPoint(npcid = npc.id.value, x = x, y = y, z = z)
            return vector3DOf(x.value, y.value, z.value)
        }

    @JvmOverloads
    fun goTo(destination: Vector3D, parameters: GoToParameters = GoToParameters.DEFAULT) {
        nativeFunctions.goTo(
                npcid = npc.id.value,
                x = destination.x,
                y = destination.y,
                z = destination.z,
                type = parameters.type.value,
                speed = parameters.speed.value,
                mode = parameters.mode.value,
                pathfinding = parameters.pathFinding.value,
                radius = parameters.radius,
                set_angle = parameters.setAngle,
                min_distance = parameters.minDistance,
                stopdelay = parameters.stopDelay
        )
    }

    @JvmOverloads
    fun goTo(player: Player, parameters: GoToParameters = GoToParameters.DEFAULT) {
        nativeFunctions.goToPlayer(
                npcid = npc.id.value,
                playerid = player.id.value,
                type = parameters.type.value,
                speed = parameters.speed.value,
                mode = parameters.mode.value,
                pathfinding = parameters.pathFinding.value,
                radius = parameters.radius,
                set_angle = parameters.setAngle,
                min_distance = parameters.minDistance,
                dist_check = parameters.distanceCheck,
                stopdelay = parameters.stopDelay
        )
    }

    fun stop() {
        nativeFunctions.stop(npc.id.value)
    }

    fun isMovingTo(player: Player): Boolean =
            nativeFunctions.isMovingAtPlayer(npcid = npc.id.value, playerid = player.id.value)

}