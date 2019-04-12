package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.PlaybackRecord
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.PlaybackRecordId

internal class PlaybackRecordRegistry(capacity: Int) :
        EntityRegistry<PlaybackRecord, PlaybackRecordId>(arrayOfNulls(capacity))