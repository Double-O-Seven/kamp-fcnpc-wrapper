package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.PlaybackRecord
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.PlaybackRecordEntityRegistry
import javax.inject.Inject

internal class PlaybackRecordFactory
@Inject
constructor(
        private val playerRecordEntityRegistry: PlaybackRecordEntityRegistry,
        private val nativeFunctions: FCNPCNativeFunctions
) {

    fun load(file: String): PlaybackRecord {
        val playbackRecord = PlaybackRecord(file, nativeFunctions)
        playerRecordEntityRegistry.register(playbackRecord)
        playbackRecord.onDestroy {
            playerRecordEntityRegistry.unregister(playbackRecord)
        }
        return playbackRecord
    }

}