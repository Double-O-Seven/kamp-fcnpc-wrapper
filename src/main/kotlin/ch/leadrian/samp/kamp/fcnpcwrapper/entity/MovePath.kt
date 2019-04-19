package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathPointId
import java.util.Collections

class MovePath
internal constructor(private val nativeFunctions: FCNPCNativeFunctions) : AbstractDestroyable(), Entity<MovePathId> {

    private val pointsById: MutableMap<MovePathPointId, MovePathPoint> = mutableMapOf()

    override val id: MovePathId
        get() = requireNotDestroyed { field }

    val points: Collection<MovePathPoint>
        get() = Collections.unmodifiableCollection(pointsById.values)

    init {
        id = MovePathId.valueOf(nativeFunctions.createMovePath())
        if (id.value == FCNPCConstants.INVALID_MOVEPATH_ID) {
            throw CreationFailedException("Could not create move path")
        }
    }

    operator fun get(movePathPointId: MovePathPointId): MovePathPoint =
            pointsById[movePathPointId]
                    ?: throw IllegalArgumentException("Invalid move path point ID ${movePathPointId.value} for move path ${id.value}")

    fun addPoint(coordinates: Vector3D): MovePathPoint {
        val pointId = nativeFunctions.addPointToMovePath(id.value, coordinates.x, coordinates.y, coordinates.z)
        val point = MovePathPoint(MovePathPointId.valueOf(pointId), this, nativeFunctions)
        pointsById[point.id] = point
        return point
    }

    internal fun onRemove(point: MovePathPoint) {
        require(point.movePath == this)
        pointsById.remove(point.id)
    }

    override fun onDestroy() {
        nativeFunctions.destroyMovePath(id.value)
    }

}