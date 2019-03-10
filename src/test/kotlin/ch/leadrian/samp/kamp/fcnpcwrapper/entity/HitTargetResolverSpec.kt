package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.MapObjectTarget
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.NoTarget
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.PlayerMapObjectTarget
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.PlayerTarget
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener.Target.VehicleTarget
import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
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
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class HitTargetResolverSpec : Spek({
    val playerService by memoized { mockk<PlayerService>() }
    val vehicleService by memoized { mockk<VehicleService>() }
    val playerMapObjectService by memoized { mockk<PlayerMapObjectService>() }
    val mapObjectService by memoized { mockk<MapObjectService>() }
    val hitTargetResolver by memoized {
        HitTargetResolver(playerService, vehicleService, playerMapObjectService, mapObjectService)
    }

    describe("getHitId") {
        context("target is PlayerTarget") {
            val playerId = 69
            val player by memoized {
                mockk<Player> {
                    every { id } returns PlayerId.valueOf(playerId)
                }
            }
            val hitTarget by memoized {
                PlayerTarget(player)
            }

            it("should return player ID") {
                assertThat(hitTargetResolver.getHitId(hitTarget))
                        .isEqualTo(playerId)
            }
        }

        context("target is VehicleTarget") {
            val vehicleId = 69
            val vehicle by memoized {
                mockk<Vehicle> {
                    every { id } returns VehicleId.valueOf(vehicleId)
                }
            }
            val hitTarget by memoized {
                VehicleTarget(vehicle)
            }

            it("should return vehicle ID") {
                assertThat(hitTargetResolver.getHitId(hitTarget))
                        .isEqualTo(vehicleId)
            }
        }

        context("target is PlayerMapObjectTarget") {
            val playerMapObjectId = 69
            val playerMapObject by memoized {
                mockk<PlayerMapObject> {
                    every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
                }
            }
            val hitTarget by memoized {
                PlayerMapObjectTarget(playerMapObject)
            }

            it("should return player map object ID") {
                assertThat(hitTargetResolver.getHitId(hitTarget))
                        .isEqualTo(playerMapObjectId)
            }
        }

        context("target is MapObjectTarget") {
            val mapObjectId = 69
            val mapObject by memoized {
                mockk<MapObject> {
                    every { id } returns MapObjectId.valueOf(mapObjectId)
                }
            }
            val hitTarget by memoized {
                MapObjectTarget(mapObject)
            }

            it("should return map object ID") {
                assertThat(hitTargetResolver.getHitId(hitTarget))
                        .isEqualTo(mapObjectId)
            }
        }

        context("no target") {
            val hitTarget by memoized { NoTarget }

            it("should return 0") {
                assertThat(hitTargetResolver.getHitId(hitTarget))
                        .isEqualTo(0)
            }
        }
    }

    describe("getHitTarget") {
        val hitId = 69

        context("hit type is PLAYER") {
            val player by memoized { mockk<Player>() }
            lateinit var hitTarget: Target

            beforeEach {
                every { playerService.getPlayer(PlayerId.valueOf(hitId)) } returns player
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.PLAYER, PlayerId.INVALID)
            }

            it("should return PlayerTarget") {
                assertThat(hitTarget)
                        .isEqualTo(PlayerTarget(player))
            }
        }

        context("hit type is VEHICLE") {
            val vehicle by memoized { mockk<Vehicle>() }
            lateinit var hitTarget: Target

            beforeEach {
                every { vehicleService.getVehicle(VehicleId.valueOf(hitId)) } returns vehicle
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.VEHICLE, PlayerId.INVALID)
            }

            it("should return VehicleTarget") {
                assertThat(hitTarget)
                        .isEqualTo(VehicleTarget(vehicle))
            }
        }

        context("hit type is PlayerMapObject") {
            val playerMapObject by memoized { mockk<PlayerMapObject>() }
            lateinit var hitTarget: Target

            beforeEach {
                val playerMapObjectOwner = PlayerId.valueOf(69)
                val player = mockk<Player>()
                every { playerService.getPlayer(playerMapObjectOwner) } returns player
                every {
                    playerMapObjectService.getPlayerMapObject(player, PlayerMapObjectId.valueOf(hitId))
                } returns playerMapObject
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.PLAYER_OBJECT, playerMapObjectOwner)
            }

            it("should return PLAYER_OBJECT") {
                assertThat(hitTarget)
                        .isEqualTo(PlayerMapObjectTarget(playerMapObject))
            }
        }

        context("hit type is OBJECT") {
            val mapObject by memoized { mockk<MapObject>() }
            lateinit var hitTarget: Target

            beforeEach {
                every { mapObjectService.getMapObject(MapObjectId.valueOf(hitId)) } returns mapObject
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.OBJECT, PlayerId.INVALID)
            }

            it("should return MapObjectTarget") {
                assertThat(hitTarget)
                        .isEqualTo(MapObjectTarget(mapObject))
            }
        }

        context("hit type is NONE") {
            lateinit var hitTarget: Target

            beforeEach {
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.NONE, PlayerId.INVALID)
            }

            it("should return PlayerTarget") {
                assertThat(hitTarget)
                        .isEqualTo(NoTarget)
            }
        }
    }
})