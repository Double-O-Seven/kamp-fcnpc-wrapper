package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.data.HitTarget
import ch.leadrian.samp.kamp.core.api.data.MapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.NoHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerMapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.VehicleHitTarget
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import javax.inject.Inject

internal class HitTargetResolver
@Inject
constructor(
        private val playerService: PlayerService,
        private val vehicleService: VehicleService,
        private val playerMapObjectService: PlayerMapObjectService,
        private val mapObjectService: MapObjectService
) {

    fun getHitId(hitTarget: HitTarget): Int {
        return when (hitTarget) {
            is PlayerHitTarget -> hitTarget.player.id.value
            is VehicleHitTarget -> hitTarget.vehicle.id.value
            is MapObjectHitTarget -> hitTarget.mapObject.id.value
            is PlayerMapObjectHitTarget -> hitTarget.playerMapObject.id.value
            else -> 0
        }
    }

    fun getHitTarget(hitId: Int, hitType: BulletHitType, playerMapObjectOwner: PlayerId): HitTarget {
        return when (hitType) {
            BulletHitType.PLAYER -> PlayerHitTarget(playerService.getPlayer(PlayerId.valueOf(hitId)))
            BulletHitType.VEHICLE -> VehicleHitTarget(vehicleService.getVehicle(VehicleId.valueOf(hitId)))
            BulletHitType.PLAYER_OBJECT -> {
                val player = playerService.getPlayer(playerMapObjectOwner)
                val playerMapObject = playerMapObjectService.getPlayerMapObject(
                        player,
                        PlayerMapObjectId.valueOf(hitId)
                )
                PlayerMapObjectHitTarget(playerMapObject)
            }
            BulletHitType.OBJECT -> MapObjectHitTarget(mapObjectService.getMapObject(MapObjectId.valueOf(hitId)))
            BulletHitType.NONE -> NoHitTarget
        }
    }

}