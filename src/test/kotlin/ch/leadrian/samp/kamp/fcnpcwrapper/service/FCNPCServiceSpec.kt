package ch.leadrian.samp.kamp.fcnpcwrapper.service

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.amx.OutputString
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MovePathFinding
import ch.leadrian.samp.kamp.fcnpcwrapper.data.WeaponInfo
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePath
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.PlaybackRecord
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.FullyControllableNPCFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.MovePathFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.PlaybackRecordFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.FullyControllableNPCRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FCNPCServiceSpec : Spek({
    val fullyControllableNPCFactory by memoized { mockk<FullyControllableNPCFactory>() }
    val fullyControllableNPCRegistry by memoized { mockk<FullyControllableNPCRegistry>() }
    val nodeLoader by memoized { mockk<NodeLoader>() }
    val movePathFactory by memoized { mockk<MovePathFactory>() }
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val playbackRecordFactory by memoized { mockk<PlaybackRecordFactory>() }
    val fcnpcService by memoized {
        FCNPCService(
                fullyControllableNPCFactory,
                fullyControllableNPCRegistry,
                nodeLoader,
                movePathFactory,
                playbackRecordFactory,
                fcnpcNativeFunctions
        )
    }

    describe("getPluginVersion") {
        beforeEach {
            every { fcnpcNativeFunctions.getPluginVersion(any(), 69) } answers {
                firstArg<OutputString>().value = "v1"
                true
            }
        }

        it("should call fcnpcService.getPluginVersion") {
            assertThat(fcnpcService.getPluginVersion(69))
                    .isEqualTo("v1")
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

    describe("setDefaultWeaponInfo") {
        beforeEach {
            every { fcnpcNativeFunctions.setWeaponDefaultInfo(any(), any(), any(), any(), any()) } returns true
        }

        context("all values are set") {
            beforeEach {
                fcnpcService.setDefaultWeaponInfo(
                        WeaponModel.M4,
                        WeaponInfo(
                                reloadTime = 123,
                                shootTime = 456,
                                clipSize = 69,
                                accuracy = 0.815f
                        )
                )
            }

            it("should call fcnpcNativeFunctions.setWeaponDefaultInfo with input values") {
                verify {
                    fcnpcNativeFunctions.setWeaponDefaultInfo(
                            weaponid = WeaponModel.M4.value,
                            reload_time = 123,
                            shoot_time = 456,
                            clip_size = 69,
                            accuracy = 0.815f
                    )
                }
            }
        }

        context("no values are set") {
            beforeEach {
                fcnpcService.setDefaultWeaponInfo(WeaponModel.M4, WeaponInfo())
            }

            it("should call fcnpcNativeFunctions.setWeaponDefaultInfo with default values") {
                verify {
                    fcnpcNativeFunctions.setWeaponDefaultInfo(
                            weaponid = WeaponModel.M4.value,
                            reload_time = -1,
                            shoot_time = -1,
                            clip_size = -1,
                            accuracy = 1f
                    )
                }
            }
        }
    }

    describe("getDefaultWeaponInfo") {
        beforeEach {
            every {
                fcnpcNativeFunctions.getWeaponDefaultInfo(
                        weaponid = WeaponModel.MINIGUN.value,
                        reload_time = any(),
                        shoot_time = any(),
                        clip_size = any(),
                        accuracy = any()
                )
            } answers {
                secondArg<MutableIntCell>().value = 123
                thirdArg<MutableIntCell>().value = 456
                arg<MutableIntCell>(3).value = 789
                arg<MutableFloatCell>(4).value = 69f
                true
            }
        }

        it("should return weapon info") {
            assertThat(fcnpcService.getDefaultWeaponInfo(WeaponModel.MINIGUN))
                    .isEqualTo(WeaponInfo(reloadTime = 123, shootTime = 456, clipSize = 789, accuracy = 69f))
        }
    }

    describe("loadPlaybackRecord") {
        val file = "my_npc.rec"
        val playbackRecord by memoized { mockk<PlaybackRecord>() }

        beforeEach {
            every { playbackRecordFactory.load(file) } returns playbackRecord
        }

        it("should return playbackRecord from factory") {
            assertThat(fcnpcService.loadPlaybackRecord(file))
                    .isEqualTo(playbackRecord)
        }
    }

    describe("createNPC") {
        val expectedNPC by memoized { mockk<FullyControllableNPC>() }
        lateinit var npc: FullyControllableNPC

        beforeEach {
            every { fullyControllableNPCFactory.create("Hans_Wurst") } returns expectedNPC
            npc = fcnpcService.createNPC("Hans_Wurst")
        }

        it("should return NPC") {
            assertThat(npc)
                    .isEqualTo(expectedNPC)
        }
    }

    describe("getAllNPCs") {
        val npc1 by memoized { mockk<FullyControllableNPC>() }
        val npc2 by memoized { mockk<FullyControllableNPC>() }
        lateinit var allNPCs: List<FullyControllableNPC>

        beforeEach {
            every { fullyControllableNPCRegistry.getAll() } returns listOf(npc1, npc2)
            allNPCs = fcnpcService.getAllNPCs()
        }

        it("should return all NPCs") {
            assertThat(allNPCs)
                    .containsExactly(npc1, npc2)
        }
    }

    describe("getNPC") {
        val npcId = FullyControllableNPCId.valueOf(1337)

        context("NPC ID is valid") {
            val expectedNPC by memoized { mockk<FullyControllableNPC>() }
            lateinit var npc: FullyControllableNPC

            beforeEach {
                every { fullyControllableNPCRegistry[npcId] } returns expectedNPC
                npc = fcnpcService.getNPC(npcId)
            }

            it("should return NPC") {
                assertThat(npc)
                        .isEqualTo(expectedNPC)
            }
        }

        context("NPC ID is invalid") {
            lateinit var caughtThrowable: Throwable

            beforeEach {
                every { fullyControllableNPCRegistry[npcId] } returns null
                caughtThrowable = catchThrowable { fcnpcService.getNPC(npcId) }
            }

            it("should throw exception") {
                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid NPC ID: 1337")
            }
        }
    }

    describe("loadNode") {
        val nodeId = NodeId.valueOf(1337)
        val expectedNode by memoized { mockk<Node>() }
        lateinit var node: Node

        beforeEach {
            every { nodeLoader.load(nodeId) } returns expectedNode
            node = fcnpcService.loadNode(nodeId)
        }

        it("should return node") {
            assertThat(node)
                    .isEqualTo(expectedNode)
        }
    }

    describe("createMovePath") {
        val expectedMovePath by memoized { mockk<MovePath>() }
        lateinit var movePath: MovePath

        beforeEach {
            every { movePathFactory.create() } returns expectedMovePath
            movePath = fcnpcService.createMovePath()
        }

        it("should return move path") {
            assertThat(movePath)
                    .isEqualTo(expectedMovePath)
        }
    }
})