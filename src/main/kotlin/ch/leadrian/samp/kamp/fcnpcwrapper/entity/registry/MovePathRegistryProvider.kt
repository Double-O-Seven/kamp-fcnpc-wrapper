package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import com.netflix.governator.annotations.Configuration
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class MovePathRegistryProvider
@Inject
constructor() : Provider<MovePathRegistry> {

    @Configuration("kamp.fcnpc.move.path.registry.capacity")
    var registryCapacity: Int = 65536

    private val registry by lazy { MovePathRegistry(registryCapacity) }

    override fun get(): MovePathRegistry = registry
}