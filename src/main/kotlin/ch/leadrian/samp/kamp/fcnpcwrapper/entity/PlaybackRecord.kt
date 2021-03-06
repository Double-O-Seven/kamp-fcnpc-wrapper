package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.PlaybackRecordId

class PlaybackRecord
internal constructor(
        val file: String,
        private val nativeFunctions: FCNPCNativeFunctions
) : AbstractDestroyable(), Entity<PlaybackRecordId> {

    override val id: PlaybackRecordId
        get() = requireNotDestroyed { field }

    init {
        id = PlaybackRecordId.valueOf(nativeFunctions.loadPlayingPlayback(file))
        if (id.value == FCNPCConstants.INVALID_RECORD_ID) {
            throw CreationFailedException("Could not load record from file: $file")
        }
    }

    override fun onDestroy() {
        nativeFunctions.unloadPlayingPlayback(id.value)
    }

}