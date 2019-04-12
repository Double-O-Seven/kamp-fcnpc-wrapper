package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathPointId

class MovePathPoint
internal constructor(
        override val id: MovePathPointId,
        val movePath: MovePath,
        private val nativeFunctions: FCNPCNativeFunctions
) : Entity<MovePathPointId> {

    private var isRemoved: Boolean = false

    val isValid: Boolean
        get() = !isRemoved && !movePath.isDestroyed

    val coordinates: Vector3D
        get() {
            val x = MutableFloatCell()
            val y = MutableFloatCell()
            val z = MutableFloatCell()
            nativeFunctions.getMovePathPoint(pathid = movePath.id.value, pointid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }

    fun removeFromPath() {
        if (isRemoved) {
            return
        }
        nativeFunctions.removePointFromMovePath(pathid = movePath.id.value, pointid = id.value)
        movePath.onRemove(this)
        isRemoved = true
    }

}