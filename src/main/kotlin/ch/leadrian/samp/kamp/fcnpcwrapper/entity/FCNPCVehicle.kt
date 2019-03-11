package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveType

class FCNPCVehicle
internal constructor(
        override val npc: FullyControllableNPC,
        private val nativeFunctions: FCNPCNativeFunctions,
        private val vehicleService: VehicleService
) : HasFullyControllableNPC {

    val current: Vehicle?
        get() {
            val vehicleId = VehicleId.valueOf(nativeFunctions.getVehicleId(npc.id.value))
            return if (vehicleService.isValidVehicle(vehicleId)) {
                vehicleService.getVehicle(vehicleId)
            } else {
                null
            }
        }

    val seat: Int?
        get() = nativeFunctions.getVehicleSeat(npc.id.value).takeIf { it != -1 }

    var isSirenUsed: Boolean
        get() = nativeFunctions.isVehicleSirenUsed(npc.id.value)
        set(value) {
            nativeFunctions.useVehicleSiren(npc.id.value, value)
        }

    var health: Float
        get() = nativeFunctions.getVehicleHealth(npc.id.value)
        set(value) {
            nativeFunctions.setVehicleHealth(npc.id.value, value)
        }

    var hydraThrustersDirection: Int
        get() = nativeFunctions.getVehicleHydraThrusters(npc.id.value)
        set(value) {
            nativeFunctions.setVehicleHydraThrusters(npcid = npc.id.value, direction = value)
        }

    var gearState: Int
        get() = nativeFunctions.getVehicleGearState(npc.id.value)
        set(value) {
            nativeFunctions.setVehicleGearState(npcid = npc.id.value, gear_state = value)
        }

    fun enter(vehicle: Vehicle, seat: Int, moveType: MoveType) {
        nativeFunctions.enterVehicle(
                npcid = npc.id.value,
                vehicleid = vehicle.id.value,
                seatid = seat,
                type = moveType.value
        )
    }

    fun exit() {
        nativeFunctions.exitVehicle(npc.id.value)
    }

    fun putIn(vehicle: Vehicle, seat: Int) {
        nativeFunctions.putInVehicle(npcid = npc.id.value, vehicleid = vehicle.id.value, seatid = seat)
    }

    fun remove() {
        nativeFunctions.removeFromVehicle(npc.id.value)
    }

}