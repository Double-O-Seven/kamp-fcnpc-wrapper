package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
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
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FCNPCSurfingSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val playerService by memoized { mockk<PlayerService>() }
    val vehicleService by memoized { mockk<VehicleService>() }
    val mapObjectService by memoized { mockk<MapObjectService>() }
    val playerMapObjectService by memoized { mockk<PlayerMapObjectService>() }
    val npcId = 123
    val npc by memoized {
        mockk<FullyControllableNPC> {
            every { id } returns FullyControllableNPCId.valueOf(npcId)
        }
    }
    val surfing by memoized {
        FCNPCSurfing(npc, fcnpcNativeFunctions, playerService, vehicleService, mapObjectService, playerMapObjectService)
    }

    describe("offset") {
        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.getSurfingOffsets(npcId, any(), any(), any()) } answers {
                    secondArg<MutableFloatCell>().value = 1f
                    thirdArg<MutableFloatCell>().value = 2f
                    arg<MutableFloatCell>(3).value = 3f
                    true
                }
            }

            it("should return offsets") {
                assertThat(surfing.offset)
                        .isEqualTo(vector3DOf(1f, 2f, 3f))
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setSurfingOffsets(any(), any(), any(), any()) } returns true
                surfing.offset = vector3DOf(1f, 2f, 3f)
            }

            it("should call fcnpcNativeFunctions.setSurfingOffsets") {
                verify { fcnpcNativeFunctions.setSurfingOffsets(npcId, 1f, 2f, 3f) }
            }
        }
    }

    describe("vehicle") {
        describe("getter") {
            context("fcnpcNativeFunctions.getSurfingVehicle returns invalid vehicle ID") {
                val vehicleId = SAMPConstants.INVALID_VEHICLE_ID

                beforeEach {
                    every { fcnpcNativeFunctions.getSurfingVehicle(npcId) } returns vehicleId
                    every { vehicleService.isValidVehicle(VehicleId.valueOf(vehicleId)) } returns false
                }

                it("should return null") {
                    assertThat(surfing.vehicle)
                            .isNull()
                }
            }

            context("fcnpcNativeFunctions.getSurfingVehicle returns valid vehicle ID") {
                val vehicleId = 69
                val vehicle by memoized { mockk<Vehicle>() }

                beforeEach {
                    every { fcnpcNativeFunctions.getSurfingVehicle(npcId) } returns vehicleId
                    every { vehicleService.isValidVehicle(VehicleId.valueOf(vehicleId)) } returns true
                    every { vehicleService.getVehicle(VehicleId.valueOf(vehicleId)) } returns vehicle
                }

                it("should return surfing vehicle") {
                    assertThat(surfing.vehicle)
                            .isEqualTo(vehicle)
                }
            }
        }

        describe("setter") {
            val vehicleId = 69
            val vehicle by memoized {
                mockk<Vehicle> {
                    every { id } returns VehicleId.valueOf(vehicleId)
                }
            }

            beforeEach {
                every { fcnpcNativeFunctions.setSurfingVehicle(any(), any()) } returns true
                surfing.vehicle = vehicle
            }

            it("should call fcnpcNativeFunctions.setSurfingVehicle") {
                verify { fcnpcNativeFunctions.setSurfingVehicle(npcid = npcId, vehicleid = vehicleId) }
            }
        }
    }

    describe("mapObject") {
        describe("getter") {
            context("fcnpcNativeFunctions.getSurfingObject returns invalid map object ID") {
                val mapObjectId = SAMPConstants.INVALID_OBJECT_ID

                beforeEach {
                    every { fcnpcNativeFunctions.getSurfingObject(npcId) } returns mapObjectId
                    every { mapObjectService.isValidMapObject(MapObjectId.valueOf(mapObjectId)) } returns false
                }

                it("should return null") {
                    assertThat(surfing.mapObject)
                            .isNull()
                }
            }

            context("fcnpcNativeFunctions.getSurfingObject returns valid map object ID") {
                val mapObjectId = 69
                val mapObject by memoized { mockk<MapObject>() }

                beforeEach {
                    every { fcnpcNativeFunctions.getSurfingObject(npcId) } returns mapObjectId
                    every { mapObjectService.isValidMapObject(MapObjectId.valueOf(mapObjectId)) } returns true
                    every { mapObjectService.getMapObject(MapObjectId.valueOf(mapObjectId)) } returns mapObject
                }

                it("should return surfing map object") {
                    assertThat(surfing.mapObject)
                            .isEqualTo(mapObject)
                }
            }
        }

        describe("setter") {
            val mapObjectId = 69
            val mapObject by memoized {
                mockk<MapObject> {
                    every { id } returns MapObjectId.valueOf(mapObjectId)
                }
            }

            beforeEach {
                every { fcnpcNativeFunctions.setSurfingObject(any(), any()) } returns true
                surfing.mapObject = mapObject
            }

            it("should call fcnpcNativeFunctions.setSurfingObject") {
                verify { fcnpcNativeFunctions.setSurfingObject(npcid = npcId, objectid = mapObjectId) }
            }
        }
    }

    describe("playerMapObject") {
        val player by memoized { mockk<Player>() }

        beforeEach {
            every { playerService.getPlayer(PlayerId.valueOf(npcId)) } returns player
        }

        describe("getter") {
            context("fcnpcNativeFunctions.getSurfingPlayerObject returns invalid player map object ID") {
                val playerMapObjectId = SAMPConstants.INVALID_OBJECT_ID

                beforeEach {
                    every { fcnpcNativeFunctions.getSurfingPlayerObject(npcId) } returns playerMapObjectId
                    every {
                        playerMapObjectService.isValidPlayerMapObject(
                                player,
                                PlayerMapObjectId.valueOf(playerMapObjectId)
                        )
                    } returns false
                }

                it("should return null") {
                    assertThat(surfing.playerMapObject)
                            .isNull()
                }
            }

            context("fcnpcNativeFunctions.getSurfingPlayerObject returns valid player map object ID") {
                val playerMapObjectId = 69
                val playerMapObject by memoized { mockk<PlayerMapObject>() }

                beforeEach {
                    every { fcnpcNativeFunctions.getSurfingPlayerObject(npcId) } returns playerMapObjectId
                    every {
                        playerMapObjectService.isValidPlayerMapObject(
                                player,
                                PlayerMapObjectId.valueOf(playerMapObjectId)
                        )
                    } returns true
                    every {
                        playerMapObjectService.getPlayerMapObject(
                                player,
                                PlayerMapObjectId.valueOf(playerMapObjectId)
                        )
                    } returns playerMapObject
                }

                it("should return surfing playerMapObject") {
                    assertThat(surfing.playerMapObject)
                            .isEqualTo(playerMapObject)
                }
            }
        }

        describe("setter") {
            val playerMapObjectId = 69
            val playerMapObject by memoized {
                mockk<PlayerMapObject> {
                    every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
                }
            }

            beforeEach {
                every { fcnpcNativeFunctions.setSurfingPlayerObject(any(), any()) } returns true
                surfing.playerMapObject = playerMapObject
            }

            it("should call fcnpcNativeFunctions.setSurfingPlayerObject") {
                verify { fcnpcNativeFunctions.setSurfingPlayerObject(npcid = npcId, objectid = playerMapObjectId) }
            }
        }
    }

    describe("giveOffset") {
        beforeEach {
            every { fcnpcNativeFunctions.giveSurfingOffsets(any(), any(), any(), any()) } returns true
            surfing.giveOffset(vector3DOf(1f, 2f, 3f))
        }

        it("should call fcnpcNativeFunctions.giveSurfingOffsets") {
            verify { fcnpcNativeFunctions.giveSurfingOffsets(npcid = npcId, x = 1f, y = 2f, z = 3f) }
        }
    }

    describe("stop") {
        beforeEach {
            every { fcnpcNativeFunctions.stopSurfing(any()) } returns true
            surfing.stop()
        }

        it("should call fcnpcNativeFunctions.stopSurfing") {
            verify { fcnpcNativeFunctions.stopSurfing(npcId) }
        }
    }
})