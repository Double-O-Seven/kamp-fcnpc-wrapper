package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnVehicleEntryCompleteListener {

    fun onVehicleEntryComplete(npc: FullyControllableNPC, vehicle: Vehicle, seatId: Int)

}