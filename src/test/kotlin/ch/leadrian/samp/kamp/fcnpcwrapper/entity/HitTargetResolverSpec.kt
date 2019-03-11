package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.data.HitTarget
import ch.leadrian.samp.kamp.core.api.data.MapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.NoHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerMapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.VehicleHitTarget
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
                PlayerHitTarget(player)
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
                VehicleHitTarget(vehicle)
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
                PlayerMapObjectHitTarget(playerMapObject)
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
                MapObjectHitTarget(mapObject)
            }

            it("should return map object ID") {
                assertThat(hitTargetResolver.getHitId(hitTarget))
                        .isEqualTo(mapObjectId)
            }
        }

        context("no target") {
            val hitTarget by memoized { NoHitTarget }

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
            lateinit var hitTarget: HitTarget

            beforeEach {
                every { playerService.getPlayer(PlayerId.valueOf(hitId)) } returns player
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.PLAYER, PlayerId.INVALID)
            }

            it("should return PlayerTarget") {
                assertThat(hitTarget)
                        .isEqualTo(PlayerHitTarget(player))
            }
        }

        context("hit type is VEHICLE") {
            val vehicle by memoized { mockk<Vehicle>() }
            lateinit var hitTarget: HitTarget

            beforeEach {
                every { vehicleService.getVehicle(VehicleId.valueOf(hitId)) } returns vehicle
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.VEHICLE, PlayerId.INVALID)
            }

            it("should return VehicleTarget") {
                assertThat(hitTarget)
                        .isEqualTo(VehicleHitTarget(vehicle))
            }
        }

        context("hit type is PlayerMapObject") {
            val playerMapObject by memoized { mockk<PlayerMapObject>() }
            lateinit var hitTarget: HitTarget

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
                        .isEqualTo(PlayerMapObjectHitTarget(playerMapObject))
            }
        }

        context("hit type is OBJECT") {
            val mapObject by memoized { mockk<MapObject>() }
            lateinit var hitTarget: HitTarget

            beforeEach {
                every { mapObjectService.getMapObject(MapObjectId.valueOf(hitId)) } returns mapObject
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.OBJECT, PlayerId.INVALID)
            }

            it("should return MapObjectTarget") {
                assertThat(hitTarget)
                        .isEqualTo(MapObjectHitTarget(mapObject))
            }
        }

        context("hit type is NONE") {
            lateinit var hitTarget: HitTarget

            beforeEach {
                hitTarget = hitTargetResolver.getHitTarget(hitId, BulletHitType.NONE, PlayerId.INVALID)
            }

            it("should return PlayerTarget") {
                assertThat(hitTarget)
                        .isEqualTo(NoHitTarget)
            }
        }
    }
})