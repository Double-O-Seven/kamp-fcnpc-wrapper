package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.PlaybackRecord
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.PlaybackRecordId
import javax.inject.Inject

internal class PlaybackRecordRegistry
@Inject
constructor(playerService: PlayerService) :
        EntityRegistry<PlaybackRecord, PlaybackRecordId>(arrayOfNulls(playerService.getMaxPlayers()))