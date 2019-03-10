package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.MapObjectTarget
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.NoTarget
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.PlayerMapObjectTarget
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.PlayerTarget
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.VehicleTarget
import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
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

    fun getHitId(hitTarget: Target): Int {
        return when (hitTarget) {
            is PlayerTarget -> hitTarget.player.id.value
            is VehicleTarget -> hitTarget.vehicle.id.value
            is MapObjectTarget -> hitTarget.mapObject.id.value
            is PlayerMapObjectTarget -> hitTarget.playerMapObject.id.value
            else -> 0
        }
    }

    fun getHitTarget(hitId: Int, hitType: BulletHitType, playerMapObjectOwner: PlayerId): Target {
        return when (hitType) {
            BulletHitType.PLAYER -> PlayerTarget(playerService.getPlayer(PlayerId.valueOf(hitId)))
            BulletHitType.VEHICLE -> VehicleTarget(vehicleService.getVehicle(VehicleId.valueOf(hitId)))
            BulletHitType.PLAYER_OBJECT -> {
                val player = playerService.getPlayer(playerMapObjectOwner)
                val playerMapObject = playerMapObjectService.getPlayerMapObject(
                        player,
                        PlayerMapObjectId.valueOf(hitId)
                )
                PlayerMapObjectTarget(playerMapObject)
            }
            BulletHitType.OBJECT -> MapObjectTarget(mapObjectService.getMapObject(MapObjectId.valueOf(hitId)))
            BulletHitType.NONE -> NoTarget
        }
    }

}