package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCSurfing
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import javax.inject.Inject

internal class FCNPCSurfingFactory
@Inject
constructor(
        private val nativeFunctions: FCNPCNativeFunctions,
        private val playerService: PlayerService,
        private val vehicleService: VehicleService,
        private val mapObjectService: MapObjectService,
        private val playerMapObjectService: PlayerMapObjectService
) {

    fun create(npc: FullyControllableNPC): FCNPCSurfing =
            FCNPCSurfing(npc, nativeFunctions, playerService, vehicleService, mapObjectService, playerMapObjectService)

}