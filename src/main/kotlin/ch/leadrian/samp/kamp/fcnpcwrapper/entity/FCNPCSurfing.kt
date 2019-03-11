package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions

class FCNPCSurfing
internal constructor(
        override val npc: FullyControllableNPC,
        private val nativeFunctions: FCNPCNativeFunctions,
        private val playerService: PlayerService,
        private val vehicleService: VehicleService,
        private val mapObjectService: MapObjectService,
        private val playerMapObjectService: PlayerMapObjectService
) : HasFullyControllableNPC {

    var offset: Vector3D
        get() {
            val x = MutableFloatCell()
            val y = MutableFloatCell()
            val z = MutableFloatCell()
            nativeFunctions.getSurfingOffsets(npcid = npc.id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctions.setSurfingOffsets(npcid = npc.id.value, x = value.x, y = value.y, z = value.z)
        }

    var vehicle: Vehicle?
        get() {
            val vehicleId = VehicleId.valueOf(nativeFunctions.getSurfingVehicle(npc.id.value))
            return if (vehicleService.isValidVehicle(vehicleId)) {
                vehicleService.getVehicle(vehicleId)
            } else {
                null
            }
        }
        set(value) {
            if (value != null) {
                nativeFunctions.setSurfingVehicle(npcid = npc.id.value, vehicleid = value.id.value)
            }
        }

    var mapObject: MapObject?
        get() {
            val mapObjectId = MapObjectId.valueOf(nativeFunctions.getSurfingObject(npc.id.value))
            return if (mapObjectService.isValidMapObject(mapObjectId)) {
                mapObjectService.getMapObject(mapObjectId)
            } else {
                null
            }
        }
        set(value) {
            if (value != null) {
                nativeFunctions.setSurfingObject(npcid = npc.id.value, objectid = value.id.value)
            }
        }

    var playerMapObject: PlayerMapObject?
        get() {
            val player = playerService.getPlayer(PlayerId.valueOf(npc.id.value))
            val playerMapObjectId = PlayerMapObjectId.valueOf(nativeFunctions.getSurfingPlayerObject(npc.id.value))
            return if (playerMapObjectService.isValidPlayerMapObject(player, playerMapObjectId)) {
                playerMapObjectService.getPlayerMapObject(player, playerMapObjectId)
            } else {
                null
            }
        }
        set(value) {
            if (value != null) {
                nativeFunctions.setSurfingPlayerObject(npcid = npc.id.value, objectid = value.id.value)
            }
        }

    fun giveOffset(offset: Vector3D) {
        nativeFunctions.giveSurfingOffsets(npcid = npc.id.value, x = offset.x, y = offset.y, z = offset.z)
    }

    fun stop() {
        nativeFunctions.stopSurfing(npc.id.value)
    }
}