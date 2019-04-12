package ch.leadrian.samp.kamp.fcnpcwrapper.service

import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.NodeRegistry
import javax.inject.Inject

internal class NodeLoader
@Inject
constructor(
        private val nodeRegistry: NodeRegistry,
        private val nativeFunctions: FCNPCNativeFunctions
) {

    fun load(nodeId: NodeId): Node = nodeRegistry[nodeId] ?: createNode(nodeId)

    private fun createNode(nodeId: NodeId): Node =
            Node(nodeId, nativeFunctions).also { nodeRegistry.register(it) }

}