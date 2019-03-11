package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCVehicle
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import javax.inject.Inject

internal class FCNPCVehicleFactory
@Inject
constructor(
        private val nativeFunctions: FCNPCNativeFunctions,
        private val vehicleService: VehicleService
) {

    fun create(npc: FullyControllableNPC): FCNPCVehicle = FCNPCVehicle(npc, nativeFunctions, vehicleService)

}