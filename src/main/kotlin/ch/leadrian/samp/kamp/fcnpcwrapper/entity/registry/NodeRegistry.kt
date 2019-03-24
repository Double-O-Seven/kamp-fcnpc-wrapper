package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId

internal class NodeRegistry : EntityRegistry<Node, NodeId>(arrayOfNulls(FCNPCConstants.FCNPC_MAX_NODES))