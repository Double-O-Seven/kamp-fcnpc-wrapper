package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePath
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathId

internal class MovePathRegistry(capacity: Int) : EntityRegistry<MovePath, MovePathId>(arrayOfNulls(capacity))