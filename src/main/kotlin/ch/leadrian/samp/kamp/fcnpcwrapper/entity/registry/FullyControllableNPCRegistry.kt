package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FullyControllableNPCRegistry
@Inject
constructor(playerService: PlayerService) :
        EntityRegistry<FullyControllableNPC, FullyControllableNPCId>(arrayOfNulls(playerService.getMaxPlayers()))