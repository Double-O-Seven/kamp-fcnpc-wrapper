package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import ch.leadrian.samp.kamp.core.api.entity.id.EntityId

data class MovePathId internal constructor(override val value: Int) : EntityId {

    companion object {

        private val movePathIds: Array<MovePathId> = (0..999).map {
            MovePathId(it)
        }.toTypedArray()

        fun valueOf(value: Int): MovePathId =
                when {
                    0 <= value && value < movePathIds.size -> movePathIds[value]
                    else -> MovePathId(value)
                }
    }
}