package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.id.EntityId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId

data class FullyControllableNPCId internal constructor(override val value: Int) : EntityId {

    fun toPlayerId(): PlayerId = PlayerId.valueOf(value)

    companion object {

        val INVALID = FullyControllableNPCId(SAMPConstants.INVALID_PLAYER_ID)

        private val fullyControllableNPCIds: Array<FullyControllableNPCId> = (0 until 1000).map {
            FullyControllableNPCId(it)
        }.toTypedArray()

        fun valueOf(value: Int): FullyControllableNPCId =
                when {
                    0 <= value && value < fullyControllableNPCIds.size -> fullyControllableNPCIds[value]
                    value == INVALID.value -> INVALID
                    else -> FullyControllableNPCId(value)
                }
    }
}

fun PlayerId.toFullyControllableNPCId(): FullyControllableNPCId = FullyControllableNPCId.valueOf(value)
