package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.PlaybackRecord
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.PlaybackRecordRegistry
import javax.inject.Inject

internal class PlaybackRecordFactory
@Inject
constructor(
        private val playerRecordRegistry: PlaybackRecordRegistry,
        private val nativeFunctions: FCNPCNativeFunctions
) {

    fun load(file: String): PlaybackRecord {
        val playbackRecord = PlaybackRecord(file, nativeFunctions)
        playerRecordRegistry.register(playbackRecord)
        playbackRecord.onDestroy {
            playerRecordRegistry.unregister(this)
        }
        return playbackRecord
    }

}