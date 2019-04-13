package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.runtime.entity.EntityResolver
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.FullyControllableNPCRegistry
import javax.inject.Inject

internal class FCNPCEntityResolverImpl
@Inject
constructor(
        private val fullyControllableNPCRegistry: FullyControllableNPCRegistry,
        entityResolver: EntityResolver
) : FCNPCEntityResolver, EntityResolver by entityResolver {

    override fun Int.toNPC(): FullyControllableNPC =
            fullyControllableNPCRegistry[this] ?: throw IllegalArgumentException("Invalid NPC ID $this")

}