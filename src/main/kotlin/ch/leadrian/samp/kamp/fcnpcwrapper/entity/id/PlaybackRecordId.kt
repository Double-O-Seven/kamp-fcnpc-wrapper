package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import ch.leadrian.samp.kamp.core.api.entity.id.EntityId
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants

data class PlaybackRecordId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = PlaybackRecordId(FCNPCConstants.INVALID_RECORD_ID)

        private val playbackRecordIds: Array<PlaybackRecordId> = (0 until 1000).map {
            PlaybackRecordId(it)
        }.toTypedArray()

        fun valueOf(value: Int): PlaybackRecordId =
                when {
                    0 <= value && value < playbackRecordIds.size -> playbackRecordIds[value]
                    value == INVALID.value -> INVALID
                    else -> PlaybackRecordId(value)
                }
    }
}