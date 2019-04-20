package ch.leadrian.samp.kamp.fcnpcwrapper

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.FCNPCCallbackProcessor
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.MovePathRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.MovePathRegistryProvider
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.PlaybackRecordRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.PlaybackRecordRegistryProvider

class FCNPCModule : KampModule() {

    override fun configure() {
        bind(FCNPCCallbackManager::class.java).asEagerSingleton()
        bind(FCNPCCallbacks::class.java).to(FCNPCCallbackProcessor::class.java)
        bind(MovePathRegistry::class.java).toProvider(MovePathRegistryProvider::class.java)
        bind(PlaybackRecordRegistry::class.java).toProvider(PlaybackRecordRegistryProvider::class.java)
    }

}