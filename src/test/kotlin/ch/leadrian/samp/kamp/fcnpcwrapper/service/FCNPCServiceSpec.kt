package ch.leadrian.samp.kamp.fcnpcwrapper.service

import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MovePathFinding
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FCNPCServiceSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val fcnpcService by memoized { FCNPCService(fcnpcNativeFunctions) }

    describe("getPluginVersion") {
        beforeEach {
            every { fcnpcNativeFunctions.getPluginVersion(any(), any()) } returns true
            // Cannot check result due to visibility of OutputString.value
            fcnpcService.getPluginVersion(69)
        }

        it("should call fcnpcService.getPluginVersion") {
            verify { fcnpcNativeFunctions.getPluginVersion(any(), 69) }
        }
    }

    describe("updateRate") {
        val updateRate = 1337

        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.getUpdateRate() } returns updateRate
            }

            it("should return update rate") {
                assertThat(fcnpcService.updateRate)
                        .isEqualTo(updateRate)
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setUpdateRate(any()) } returns true
                fcnpcService.updateRate = updateRate
            }

            it("should call fcnpcNativeFunctions.setUpdateRate") {
                verify { fcnpcNativeFunctions.setUpdateRate(updateRate) }
            }
        }
    }

    describe("tickRate") {
        val tickRate = 1337

        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.getTickRate() } returns tickRate
            }

            it("should return update rate") {
                assertThat(fcnpcService.tickRate)
                        .isEqualTo(tickRate)
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setTickRate(any()) } returns true
                fcnpcService.tickRate = tickRate
            }

            it("should call fcnpcNativeFunctions.setTickRate") {
                verify { fcnpcNativeFunctions.setTickRate(tickRate) }
            }
        }
    }

    describe("useMoveMode") {
        beforeEach {
            every { fcnpcNativeFunctions.useMoveMode(any(), any()) } returns true
        }

        MoveMode.values().forEach { moveMode ->
            listOf(true, false).forEach { use ->
                context("useMoveMode is called with $moveMode and $use") {
                    beforeEach {
                        fcnpcService.useMoveMode(moveMode, use)
                    }

                    it("should call cnpcNativeFunctions.useMoveMode with $moveMode and $use") {
                        verify { fcnpcNativeFunctions.useMoveMode(moveMode.value, use) }
                    }
                }
            }
        }

        context("useMoveMode is called only with MoveMode parameter") {
            beforeEach {
                fcnpcService.useMoveMode(MoveMode.AUTO)
            }

            it("should use true as default value for use parameter") {
                verify { fcnpcNativeFunctions.useMoveMode(MoveMode.AUTO.value, true) }
            }
        }
    }

    describe("isMoveModeUsed") {
        listOf(true, false).forEach { isUsed ->
            MoveMode.values().forEach { moveMode ->
                context("fcnpcNativeFunctions.isMoveModeUsed returns $isUsed for move mode $moveMode") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isMoveModeUsed(moveMode.value) } returns isUsed
                    }

                    it("should return $isUsed for move mode $moveMode") {
                        assertThat(fcnpcService.isMoveModeUsed(moveMode))
                                .isEqualTo(isUsed)
                    }
                }
            }
        }
    }

    describe("useMovePathFinding") {
        beforeEach {
            every { fcnpcNativeFunctions.useMovePathfinding(any(), any()) } returns true
        }

        MovePathFinding.values().forEach { movePathFinding ->
            listOf(true, false).forEach { use ->
                context("useMovePathFinding is called with $movePathFinding and $use") {
                    beforeEach {
                        fcnpcService.useMovePathFinding(movePathFinding, use)
                    }

                    it("should call cnpcNativeFunctions.useMovePathFinding with $movePathFinding and $use") {
                        verify { fcnpcNativeFunctions.useMovePathfinding(movePathFinding.value, use) }
                    }
                }
            }
        }

        context("useMovePathFind is only called with MovePathFind parameter") {
            beforeEach {
                fcnpcService.useMovePathFinding(MovePathFinding.AUTO)
            }

            it("should use true as default value for use parameter") {
                verify { fcnpcNativeFunctions.useMovePathfinding(MovePathFinding.AUTO.value, true) }
            }
        }
    }

    describe("isMovePathFindingUsed") {
        listOf(true, false).forEach { isUsed ->
            MovePathFinding.values().forEach { movePathFinding ->
                context("fcnpcNativeFunctions.isMovePathFindingUsed returns $isUsed for move mode $movePathFinding") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isMovePathfindingUsed(movePathFinding.value) } returns isUsed
                    }

                    it("should return $isUsed for move mode $movePathFinding") {
                        assertThat(fcnpcService.isMovePathFindingUsed(movePathFinding))
                                .isEqualTo(isUsed)
                    }
                }
            }
        }
    }

    describe("isCrashLogUsed") {
        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.useCrashLog(any()) } returns true
            }

            listOf(true, false).forEach { use ->
                context("isCrashLog is set to $use") {
                    beforeEach {
                        fcnpcService.isCrashLogUsed = use
                    }

                    it("should call fcnpcNativeFunctions.useCrashLog with value $use") {
                        verify { fcnpcNativeFunctions.useCrashLog(use) }
                    }
                }
            }
        }

        describe("getter") {
            listOf(true, false).forEach { isUsed ->
                context("fcnpcNativeFunctions.isCrashLogUsed returns $isUsed") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isCrashLogUsed() } returns isUsed
                    }

                    it("should return $isUsed") {
                        assertThat(fcnpcService.isCrashLogUsed)
                                .isEqualTo(isUsed)
                    }
                }
            }
        }
    }
})