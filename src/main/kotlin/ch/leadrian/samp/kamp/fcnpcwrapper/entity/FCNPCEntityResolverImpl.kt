package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.runtime.entity.EntityResolver
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.FullyControllableNPCRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.MovePathRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.service.NodeLoader
import javax.inject.Inject

internal class FCNPCEntityResolverImpl
@Inject
constructor(
        private val fullyControllableNPCRegistry: FullyControllableNPCRegistry,
        private val nodeLoader: NodeLoader,
        private val movePathRegistry: MovePathRegistry,
        entityResolver: EntityResolver
) : FCNPCEntityResolver, EntityResolver by entityResolver {

    override fun Int.toNPC(): FullyControllableNPC =
            fullyControllableNPCRegistry[this] ?: throw IllegalArgumentException("Invalid NPC ID $this")

    override fun Int.toNode(): Node = nodeLoader.load(NodeId.valueOf(this))

    override fun Int.toMovePath(): MovePath =
            movePathRegistry[this] ?: throw IllegalArgumentException("Invalid move path ID $this")

}