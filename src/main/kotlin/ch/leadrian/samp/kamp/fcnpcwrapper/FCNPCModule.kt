package ch.leadrian.samp.kamp.fcnpcwrapper

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.FCNPCCallbackProcessor
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCChangeHeightHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCChangeNodeHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCCreateHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCDeathHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCDestroyHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCFinishMovePathHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCFinishMovePathPointHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCFinishNodeHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCFinishNodePointHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCFinishPlaybackHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCGiveDamageHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCReachDestinationHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCRespawnHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCSpawnHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCStreamInHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCStreamOutHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCTakeDamageHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCUpdateHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCWeaponShotHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnNPCWeaponStateChangeHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnVehicleEntryCompleteHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnVehicleExitCompleteHandler
import ch.leadrian.samp.kamp.fcnpcwrapper.callback.OnVehicleTakeDamageHandler
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
        newCallbackListenerRegistrySetBinder().apply {
            addBinding().to(OnNPCChangeHeightHandler::class.java)
            addBinding().to(OnNPCChangeNodeHandler::class.java)
            addBinding().to(OnNPCCreateHandler::class.java)
            addBinding().to(OnNPCDeathHandler::class.java)
            addBinding().to(OnNPCDestroyHandler::class.java)
            addBinding().to(OnNPCFinishMovePathHandler::class.java)
            addBinding().to(OnNPCFinishMovePathPointHandler::class.java)
            addBinding().to(OnNPCFinishNodeHandler::class.java)
            addBinding().to(OnNPCFinishNodePointHandler::class.java)
            addBinding().to(OnNPCFinishPlaybackHandler::class.java)
            addBinding().to(OnNPCGiveDamageHandler::class.java)
            addBinding().to(OnNPCReachDestinationHandler::class.java)
            addBinding().to(OnNPCRespawnHandler::class.java)
            addBinding().to(OnNPCSpawnHandler::class.java)
            addBinding().to(OnNPCStreamInHandler::class.java)
            addBinding().to(OnNPCStreamOutHandler::class.java)
            addBinding().to(OnNPCTakeDamageHandler::class.java)
            addBinding().to(OnNPCUpdateHandler::class.java)
            addBinding().to(OnNPCWeaponShotHandler::class.java)
            addBinding().to(OnNPCWeaponStateChangeHandler::class.java)
            addBinding().to(OnVehicleEntryCompleteHandler::class.java)
            addBinding().to(OnVehicleExitCompleteHandler::class.java)
            addBinding().to(OnVehicleTakeDamageHandler::class.java)
        }
    }

}