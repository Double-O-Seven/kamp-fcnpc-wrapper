package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import com.netflix.governator.annotations.Configuration
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class PlaybackRecordRegistryProvider
@Inject
constructor() : Provider<PlaybackRecordRegistry> {

    @Configuration("kamp.fcnpc.playback.record.registry.capacity")
    var registryCapacity: Int = 4096

    private val registry by lazy { PlaybackRecordRegistry(registryCapacity) }

    override fun get(): PlaybackRecordRegistry = registry
}