package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NodeRegistry
@Inject
constructor() : EntityRegistry<Node, NodeId>(arrayOfNulls(FCNPCConstants.MAX_NODES))