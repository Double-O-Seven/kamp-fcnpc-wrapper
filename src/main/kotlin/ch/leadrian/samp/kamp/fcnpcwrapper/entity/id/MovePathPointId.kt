package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import ch.leadrian.samp.kamp.core.api.entity.id.EntityId

data class MovePathPointId internal constructor(override val value: Int) : EntityId {

    companion object {

        private val movePathPointIds: Array<MovePathPointId> = (0..999).map {
            MovePathPointId(it)
        }.toTypedArray()

        fun valueOf(value: Int): MovePathPointId =
                when {
                    0 <= value && value < movePathPointIds.size -> movePathPointIds[value]
                    else -> MovePathPointId(value)
                }
    }
}