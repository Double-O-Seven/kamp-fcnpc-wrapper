package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveType
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FCNPCVehicleSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val vehicleService by memoized { mockk<VehicleService>() }
    val npcId = 123
    val npc by memoized {
        mockk<FullyControllableNPC> {
            every { id } returns FullyControllableNPCId.valueOf(npcId)
        }
    }
    val fcnpcVehicle by memoized { FCNPCVehicle(npc, fcnpcNativeFunctions, vehicleService) }

    describe("current") {
        context("given vehicle does not exist") {
            val vehicleId = SAMPConstants.INVALID_VEHICLE_ID

            beforeEach {
                every { fcnpcNativeFunctions.getVehicleId(npcId) } returns vehicleId
                every { vehicleService.isValidVehicle(VehicleId.valueOf(vehicleId)) } returns false
            }

            it("should return null") {
                assertThat(fcnpcVehicle.current)
                        .isNull()
            }
        }

        context("vehicle exists") {
            val vehicleId = 69
            val vehicle by memoized { mockk<Vehicle>() }

            beforeEach {
                every { fcnpcNativeFunctions.getVehicleId(npcId) } returns vehicleId
                every { vehicleService.isValidVehicle(VehicleId.valueOf(vehicleId)) } returns true
                every { vehicleService.getVehicle(VehicleId.valueOf(vehicleId)) } returns vehicle
            }

            it("should return vehicle") {
                assertThat(fcnpcVehicle.current)
                        .isEqualTo(vehicle)
            }
        }
    }

    describe("seat") {
        context("seat is valid") {
            val seat = 3

            beforeEach {
                every { fcnpcNativeFunctions.getVehicleSeat(npcId) } returns seat
            }

            it("should return seat") {
                assertThat(fcnpcVehicle.seat)
                        .isEqualTo(seat)
            }
        }

        context("seat is not valid") {
            beforeEach {
                every { fcnpcNativeFunctions.getVehicleSeat(npcId) } returns -1
            }

            it("should return null") {
                assertThat(fcnpcVehicle.seat)
                        .isNull()
            }
        }
    }

    describe("isSirenUsed") {
        describe("getter") {
            listOf(true, false).forEach { isSirenUsed ->
                context("fcnpcNativeFunctions.isVehicleSirenUsed returns $isSirenUsed") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isVehicleSirenUsed(npcId) } returns isSirenUsed
                    }

                    it("should return $isSirenUsed") {
                        assertThat(fcnpcVehicle.isSirenUsed)
                                .isEqualTo(isSirenUsed)
                    }
                }
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.useVehicleSiren(any(), any()) } returns true
            }

            listOf(true, false).forEach { isSirenUsed ->
                context("value is set to $isSirenUsed") {
                    beforeEach {
                        fcnpcVehicle.isSirenUsed = isSirenUsed
                    }

                    it("should call fcnpcNativeFunctions.useVehicleSiren") {
                        verify { fcnpcNativeFunctions.useVehicleSiren(npcId, isSirenUsed) }
                    }
                }
            }
        }
    }

    describe("health") {
        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.getVehicleHealth(npcId) } returns 512f
            }

            it("should return value") {
                assertThat(fcnpcVehicle.health)
                        .isEqualTo(512f)
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setVehicleHealth(any(), any()) } returns true
                fcnpcVehicle.health = 69f
            }

            it("should call fcnpcNativeFunctions.setVehicleHealth") {
                verify { fcnpcNativeFunctions.setVehicleHealth(npcId, 69f) }
            }
        }
    }

    describe("hydraThrustersDirection") {
        val hydraThrustersDirection = 13

        describe("getter") {

            beforeEach {
                every { fcnpcNativeFunctions.getVehicleHydraThrusters(npcId) } returns hydraThrustersDirection
            }

            it("should return direction") {
                assertThat(fcnpcVehicle.hydraThrustersDirection)
                        .isEqualTo(hydraThrustersDirection)
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setVehicleHydraThrusters(any(), any()) } returns true
                fcnpcVehicle.hydraThrustersDirection = hydraThrustersDirection
            }

            it("should call fcnpcNativeFunctions.setVehicleHydraThrusters") {
                verify {
                    fcnpcNativeFunctions.setVehicleHydraThrusters(
                            npcid = npcId,
                            direction = hydraThrustersDirection
                    )
                }
            }
        }
    }

    describe("gearState") {
        val gearState = 13

        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.getVehicleGearState(npcId) } returns gearState
            }

            it("should return gear state") {
                assertThat(fcnpcVehicle.gearState)
                        .isEqualTo(gearState)
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setVehicleGearState(any(), any()) } returns true
                fcnpcVehicle.gearState = gearState
            }

            it("should call fcnpcNativeFunctions.setVehicleGearState") {
                verify { fcnpcNativeFunctions.setVehicleGearState(npcid = npcId, gear_state = gearState) }
            }
        }
    }

    describe("enter") {
        val vehicleId = 69
        val vehicle by memoized {
            mockk<Vehicle> {
                every { id } returns VehicleId.valueOf(vehicleId)
            }
        }

        beforeEach {
            every {
                fcnpcNativeFunctions.enterVehicle(any(), any(), any(), any())
            } returns true
            fcnpcVehicle.enter(vehicle, 3, MoveType.SPRINT)
        }

        it("should call fcnpcNativeFunctions.enterVehicle") {
            verify {
                fcnpcNativeFunctions.enterVehicle(
                        npcid = npcId,
                        vehicleid = vehicleId,
                        seatid = 3,
                        type = MoveType.SPRINT.value
                )
            }
        }
    }

    describe("exit") {
        beforeEach {
            every { fcnpcNativeFunctions.exitVehicle(any()) } returns true
            fcnpcVehicle.exit()
        }

        it("should call fcnpcNativeFunctions.exitVehicle") {
            verify { fcnpcNativeFunctions.exitVehicle(npcId) }
        }
    }

    describe("putIn") {
        val vehicleId = 69
        val vehicle by memoized {
            mockk<Vehicle> {
                every { id } returns VehicleId.valueOf(vehicleId)
            }
        }

        beforeEach {
            every { fcnpcNativeFunctions.putInVehicle(any(), any(), any()) } returns true
            fcnpcVehicle.putIn(vehicle, 3)
        }

        it("should call fcnpcNativeFunctions.putInVehicle") {
            verify { fcnpcNativeFunctions.putInVehicle(npcid = npcId, vehicleid = vehicleId, seatid = 3) }
        }
    }

    describe("remove") {
        beforeEach {
            every { fcnpcNativeFunctions.removeFromVehicle(any()) } returns true
            fcnpcVehicle.remove()
        }

        it("should call fcnpcNativeFunctions.removeFromVehicle") {
            verify { fcnpcNativeFunctions.removeFromVehicle(npcId) }
        }
    }
})