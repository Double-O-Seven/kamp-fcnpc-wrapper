package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.SpecialAction
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.playerKeysOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.quaternionOf
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MovePathFinding
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveSpeed
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveType
import ch.leadrian.samp.kamp.fcnpcwrapper.data.GoByMovePathParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.FCNPCCombatFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.FCNPCSurfingFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.FCNPCVehicleFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathPointId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.Arrays
import java.util.function.Function
import java.util.stream.Stream
import kotlin.streams.toList

internal object FullyControllableNPCSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val fcnpcCombatFactory by memoized { mockk<FCNPCCombatFactory>(relaxed = true) }
    val fcnpcVehicleFactory by memoized { mockk<FCNPCVehicleFactory>(relaxed = true) }
    val fcnpcSurfingFactory by memoized { mockk<FCNPCSurfingFactory>(relaxed = true) }

    describe("constructor") {
        val name = "JonSnow"

        context("fcnpcNativeFunctions.create returns valid ID") {
            val npcId = 69
            val npc by memoized {
                every { fcnpcNativeFunctions.create(name) } returns npcId
                FullyControllableNPC(
                        name,
                        fcnpcNativeFunctions,
                        fcnpcCombatFactory,
                        fcnpcVehicleFactory,
                        fcnpcSurfingFactory
                )
            }

            it("should initialize id") {
                assertThat(npc.id)
                        .isEqualTo(FullyControllableNPCId.valueOf(npcId))
            }

            it("should initialize name") {
                assertThat(npc.name)
                        .isEqualTo(name)
            }
        }

        context("fcnpcNativeFunctions.create returns INVALID_PLAYER_ID") {
            beforeEach {
                every { fcnpcNativeFunctions.create(name) } returns SAMPConstants.INVALID_PLAYER_ID
            }

            it("should throw exception") {
                val caughtThrowable = catchThrowable {
                    FullyControllableNPC(
                            name,
                            fcnpcNativeFunctions,
                            fcnpcCombatFactory,
                            fcnpcVehicleFactory,
                            fcnpcSurfingFactory
                    )
                }

                assertThat(caughtThrowable)
                        .isInstanceOf(CreationFailedException::class.java)
                        .hasMessage("Could not create NPC")
            }
        }
    }

    describe("constructed NPC") {
        val name = "JonSnow"
        val npcId = 69
        val npc by memoized {
            every { fcnpcNativeFunctions.create(name) } returns npcId
            FullyControllableNPC(
                    name,
                    fcnpcNativeFunctions,
                    fcnpcCombatFactory,
                    fcnpcVehicleFactory,
                    fcnpcSurfingFactory
            )
        }

        describe("id") {
            context("NPC is not destroyed") {
                it("should return record ID") {
                    assertThat(npc.id.value)
                            .isEqualTo(npcId)
                }
            }

            context("NPC is destroyed") {
                lateinit var caughtThrowable: Throwable

                beforeEach {
                    every { fcnpcNativeFunctions.destroy(any()) } returns true
                    npc.destroy()
                    caughtThrowable = catchThrowable { npc.id }
                }

                it("should throw exception") {
                    assertThat(caughtThrowable)
                            .isInstanceOf(AlreadyDestroyedException::class.java)
                }
            }
        }

        describe("isSpawned") {
            listOf(true, false).forEach { isSpawned ->
                context("fcnpcNativeFunctions.isSpawned returns $isSpawned") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isSpawned(npcId) } returns isSpawned
                    }

                    it("should return $isSpawned") {
                        assertThat(npc.isSpawned)
                                .isEqualTo(isSpawned)
                    }
                }
            }
        }

        describe("isDead") {
            listOf(true, false).forEach { isDead ->
                context("fcnpcNativeFunctions.isDead returns $isDead") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isDead(npcId) } returns isDead
                    }

                    it("should return $isDead") {
                        assertThat(npc.isDead)
                                .isEqualTo(isDead)
                    }
                }
            }
        }

        describe("isStreamedInForAnyone") {
            listOf(true, false).forEach { isStreamedIn ->
                context("fcnpcNativeFunctions.isStreamedInForAnyone returns $isStreamedIn") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isStreamedInForAnyone(npcId) } returns isStreamedIn
                    }

                    it("should return $isStreamedIn") {
                        assertThat(npc.isStreamedInForAnyone)
                                .isEqualTo(isStreamedIn)
                    }
                }
            }
        }

        describe("coordinates") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getPosition(npcId, any(), any(), any()) } answers {
                        secondArg<MutableFloatCell>().value = 1f
                        thirdArg<MutableFloatCell>().value = 2f
                        arg<MutableFloatCell>(3).value = 3f
                        true
                    }
                }

                it("should return 3D vector with coordinates ") {
                    assertThat(npc.coordinates)
                            .isEqualTo(vector3DOf(1f, 2f, 3f))
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setPosition(any(), any(), any(), any()) } returns true
                    npc.coordinates = vector3DOf(1f, 2f, 3f)
                }

                it("should call fcnpcNativeFunctions.setPosition") {
                    verify { fcnpcNativeFunctions.setPosition(npcId, 1f, 2f, 3f) }
                }
            }
        }

        describe("angle") {
            describe("getter") {

                beforeEach {
                    every { fcnpcNativeFunctions.getAngle(npcId) } returns 13.37f
                }

                it("should return angle") {
                    assertThat(npc.angle)
                            .isEqualTo(13.37f)
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setAngle(any(), any()) } returns true
                    npc.angle = 13.37f
                }

                it("should call fcnpcNativeFunctions.setAngle") {
                    verify { fcnpcNativeFunctions.setAngle(npcId, 13.37f) }
                }
            }
        }

        describe("quaternion") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getQuaternion(npcId, any(), any(), any(), any()) } answers {
                        secondArg<MutableFloatCell>().value = 1f
                        thirdArg<MutableFloatCell>().value = 2f
                        arg<MutableFloatCell>(3).value = 3f
                        arg<MutableFloatCell>(4).value = 4f
                        true
                    }
                }

                it("should return quaternion") {
                    assertThat(npc.quaternion)
                            .isEqualTo(quaternionOf(w = 1f, x = 2f, y = 3f, z = 4f))
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setQuaternion(any(), any(), any(), any(), any()) } returns true
                    npc.quaternion = quaternionOf(w = 1f, x = 2f, y = 3f, z = 4f)
                }

                it("should call fcnpcNativeFunctions.setPosition") {
                    verify { fcnpcNativeFunctions.setQuaternion(npcid = npcId, w = 1f, x = 2f, y = 3f, z = 4f) }
                }
            }
        }

        describe("velocity") {
            beforeEach {
                every { fcnpcNativeFunctions.getVelocity(npcId, any(), any(), any()) } answers {
                    secondArg<MutableFloatCell>().value = 1f
                    thirdArg<MutableFloatCell>().value = 2f
                    arg<MutableFloatCell>(3).value = 3f
                    true
                }
            }

            it("should return velocity vector") {
                assertThat(npc.velocity)
                        .isEqualTo(vector3DOf(1f, 2f, 3f))
            }
        }

        describe("speed") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getSpeed(npcId) } returns 13.37f
                }

                it("should return speed") {
                    assertThat(npc.speed)
                            .isEqualTo(13.37f)
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setSpeed(any(), any()) } returns true
                    npc.speed = 69f
                }

                it("should call fcnpcNativeFunctions.setSpeed") {
                    verify { fcnpcNativeFunctions.setSpeed(npcId, 69f) }
                }
            }
        }

        describe("interiorId") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getInterior(npcId) } returns 13
                }

                it("should return interior ID") {
                    assertThat(npc.interiorId)
                            .isEqualTo(13)
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setInterior(any(), any()) } returns true
                    npc.interiorId = 69
                }

                it("should call fcnpcNativeFunctions.setInteriorId") {
                    verify { fcnpcNativeFunctions.setInterior(npcId, 69) }
                }
            }
        }

        describe("virtualWorldId") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getVirtualWorld(npcId) } returns 13
                }

                it("should return virtual world ID") {
                    assertThat(npc.virtualWorldId)
                            .isEqualTo(13)
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setVirtualWorld(any(), any()) } returns true
                    npc.virtualWorldId = 69
                }

                it("should call fcnpcNativeFunctions.setVirtualWorldId") {
                    verify { fcnpcNativeFunctions.setVirtualWorld(npcId, 69) }
                }
            }
        }

        describe("position") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getPosition(npcId, any(), any(), any()) } answers {
                        secondArg<MutableFloatCell>().value = 1f
                        thirdArg<MutableFloatCell>().value = 2f
                        arg<MutableFloatCell>(3).value = 3f
                        true
                    }
                    every { fcnpcNativeFunctions.getAngle(npcId) } returns 13.37f
                }

                it("should return 3D vector with coordinates ") {
                    assertThat(npc.position)
                            .isEqualTo(positionOf(1f, 2f, 3f, 13.37f))
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setPosition(any(), any(), any(), any()) } returns true
                    every { fcnpcNativeFunctions.setAngle(any(), any()) } returns true
                    npc.position = positionOf(1f, 2f, 3f, 13.37f)
                }

                it("should call fcnpcNativeFunctions.setPosition") {
                    verify { fcnpcNativeFunctions.setPosition(npcId, 1f, 2f, 3f) }
                }

                it("should call fcnpcNativeFunctions.setAngle") {
                    verify { fcnpcNativeFunctions.setAngle(npcId, 13.37f) }
                }
            }
        }

        describe("location") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getPosition(npcId, any(), any(), any()) } answers {
                        secondArg<MutableFloatCell>().value = 1f
                        thirdArg<MutableFloatCell>().value = 2f
                        arg<MutableFloatCell>(3).value = 3f
                        true
                    }
                    every { fcnpcNativeFunctions.getInterior(npcId) } returns 69
                    every { fcnpcNativeFunctions.getVirtualWorld(npcId) } returns 1234
                }

                it("should return location") {
                    assertThat(npc.location)
                            .isEqualTo(locationOf(x = 1f, y = 2f, z = 3f, interiorId = 69, worldId = 1234))
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setPosition(any(), any(), any(), any()) } returns true
                    every { fcnpcNativeFunctions.setInterior(any(), any()) } returns true
                    every { fcnpcNativeFunctions.setVirtualWorld(any(), any()) } returns true
                    npc.location = locationOf(x = 1f, y = 2f, z = 3f, interiorId = 69, worldId = 1234)
                }

                it("should call fcnpcNativeFunctions.setPosition") {
                    verify { fcnpcNativeFunctions.setPosition(npcId, 1f, 2f, 3f) }
                }

                it("should call fcnpcNativeFunctions.setInterior") {
                    verify { fcnpcNativeFunctions.setInterior(npcId, 69) }
                }

                it("should call fcnpcNativeFunctions.setVirtualWorld") {
                    verify { fcnpcNativeFunctions.setVirtualWorld(npcId, 1234) }
                }
            }
        }

        describe("angledLocation") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getPosition(npcId, any(), any(), any()) } answers {
                        secondArg<MutableFloatCell>().value = 1f
                        thirdArg<MutableFloatCell>().value = 2f
                        arg<MutableFloatCell>(3).value = 3f
                        true
                    }
                    every { fcnpcNativeFunctions.getAngle(npcId) } returns 13.37f
                    every { fcnpcNativeFunctions.getInterior(npcId) } returns 69
                    every { fcnpcNativeFunctions.getVirtualWorld(npcId) } returns 1234
                }

                it("should return location") {
                    assertThat(npc.angledLocation)
                            .isEqualTo(
                                    angledLocationOf(
                                            x = 1f,
                                            y = 2f,
                                            z = 3f,
                                            angle = 13.37f,
                                            interiorId = 69,
                                            worldId = 1234
                                    )
                            )
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setPosition(any(), any(), any(), any()) } returns true
                    every { fcnpcNativeFunctions.setAngle(any(), any()) } returns true
                    every { fcnpcNativeFunctions.setInterior(any(), any()) } returns true
                    every { fcnpcNativeFunctions.setVirtualWorld(any(), any()) } returns true
                    npc.angledLocation = angledLocationOf(
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            angle = 13.37f,
                            interiorId = 69,
                            worldId = 1234
                    )
                }

                it("should call fcnpcNativeFunctions.setPosition") {
                    verify { fcnpcNativeFunctions.setPosition(npcId, 1f, 2f, 3f) }
                }

                it("should call fcnpcNativeFunctions.setAngle") {
                    verify { fcnpcNativeFunctions.setAngle(npcId, 13.37f) }
                }

                it("should call fcnpcNativeFunctions.setInterior") {
                    verify { fcnpcNativeFunctions.setInterior(npcId, 69) }
                }

                it("should call fcnpcNativeFunctions.setVirtualWorld") {
                    verify { fcnpcNativeFunctions.setVirtualWorld(npcId, 1234) }
                }
            }
        }

        describe("health") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getHealth(npcId) } returns 69f
                }

                it("should return health") {
                    assertThat(npc.health)
                            .isEqualTo(69f)
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setHealth(any(), any()) } returns true
                    npc.health = 69f
                }

                it("should call fcnpcNativeFunctions.setHealth") {
                    verify { fcnpcNativeFunctions.setHealth(npcId, 69f) }
                }
            }
        }

        describe("armour") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getArmour(npcId) } returns 69f
                }

                it("should return armour") {
                    assertThat(npc.armour)
                            .isEqualTo(69f)
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setArmour(any(), any()) } returns true
                    npc.armour = 69f
                }

                it("should call fcnpcNativeFunctions.setArmour") {
                    verify { fcnpcNativeFunctions.setArmour(npcId, 69f) }
                }
            }
        }

        describe("isInvulnerable") {
            describe("getter") {
                listOf(true, false).forEach { isInvulnerable ->
                    context("fcnpcNativeFunctions.isInvulnerable returns $isInvulnerable") {
                        beforeEach {
                            every { fcnpcNativeFunctions.isInvulnerable(npcId) } returns isInvulnerable
                        }

                        it("should return $isInvulnerable") {
                            assertThat(npc.isInvulnerable)
                                    .isEqualTo(isInvulnerable)
                        }
                    }
                }
            }

            describe("setter") {
                listOf(true, false).forEach { isInvulnerable ->
                    context("value is set to $isInvulnerable") {
                        beforeEach {
                            every { fcnpcNativeFunctions.setInvulnerable(any(), any()) } returns true
                            npc.isInvulnerable = isInvulnerable
                        }

                        it("should call fcnpcNativeFunctions.setInvulnerable with value $isInvulnerable") {
                            verify { fcnpcNativeFunctions.setInvulnerable(npcId, isInvulnerable) }
                        }
                    }
                }
            }
        }

        describe("skinModel") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getSkin(npcId) } returns SkinModel.BALLAS1.value
                }

                it("should return skin model") {
                    assertThat(npc.skinModel)
                            .isEqualTo(SkinModel.BALLAS1)
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setSkin(any(), any()) } returns true
                    npc.skinModel = SkinModel.BALLAS1
                }

                it("should call fcnpcNativeFunctions.setSkin") {
                    verify { fcnpcNativeFunctions.setSkin(npcId, SkinModel.BALLAS1.value) }
                }
            }
        }

        describe("keys") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getKeys(npcId, any(), any(), any()) } answers {
                        secondArg<MutableIntCell>().value = 123
                        thirdArg<MutableIntCell>().value = 456
                        arg<MutableIntCell>(3).value = 789
                        true
                    }
                }

                it("should return keys") {
                    assertThat(npc.keys)
                            .isEqualTo(playerKeysOf(upDown = 123, leftRight = 456, keys = 789))
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setKeys(any(), any(), any(), any()) } returns true
                    npc.keys = playerKeysOf(upDown = 123, leftRight = 456, keys = 789)
                }

                it("should call fcnpcNativeFunctions.setKeys") {
                    verify { fcnpcNativeFunctions.setKeys(npcid = npcId, ud_analog = 123, lr_analog = 456, keys = 789) }
                }
            }
        }

        describe("specialAction") {
            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getSpecialAction(npcId) } returns SpecialAction.USE_JETPACK.value
                }

                it("should return special action") {
                    assertThat(npc.specialAction)
                            .isEqualTo(SpecialAction.USE_JETPACK)
                }
            }

            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setSpecialAction(any(), any()) } returns true
                    npc.specialAction = SpecialAction.SMOKE_CIGGY
                }

                it("should call fcnpcNativeFunctions.setSpecialAction") {
                    verify {
                        fcnpcNativeFunctions.setSpecialAction(npcid = npcId, actionid = SpecialAction.SMOKE_CIGGY.value)
                    }
                }
            }
        }

        describe("isPlayingNode") {
            listOf(true, false).forEach { isPlayingNode ->
                context("fcnpcNativeFunctions.isPlayingNode returns $isPlayingNode") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isPlayingNode(npcId) } returns isPlayingNode
                    }

                    it("should return $isPlayingNode") {
                        assertThat(npc.isPlayingNode)
                                .isEqualTo(isPlayingNode)
                    }
                }
            }
        }

        describe("isPlayingNodePaused") {
            listOf(true, false).forEach { isPlayingNodePaused ->
                context("fcnpcNativeFunctions.isPlayingNodePaused returns $isPlayingNodePaused") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isPlayingNodePaused(npcId) } returns isPlayingNodePaused
                    }

                    it("should return $isPlayingNodePaused") {
                        assertThat(npc.isPlayingNodePaused)
                                .isEqualTo(isPlayingNodePaused)
                    }
                }
            }
        }

        describe("moveMode") {
            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setMoveMode(any(), any()) } returns true
                    npc.moveMode = MoveMode.COL_ANDREAS
                }

                it("should call fcnpcNativeFunctions.setMoveMode") {
                    verify { fcnpcNativeFunctions.setMoveMode(npcid = npcId, mode = MoveMode.COL_ANDREAS.value) }
                }
            }

            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getMoveMode(npcId) } returns MoveMode.COL_ANDREAS.value
                }

                it("should return move mode") {
                    assertThat(npc.moveMode)
                            .isEqualTo(MoveMode.COL_ANDREAS)
                }
            }
        }

        describe("minHeightCallbackThreshold") {
            describe("setter") {
                beforeEach {
                    every { fcnpcNativeFunctions.setMinHeightPosCall(any(), any()) } returns true
                    npc.minHeightCallbackThreshold = 13.37f
                }

                it("should call fcnpcNativeFunctions.setMinHeightPosCall") {
                    verify { fcnpcNativeFunctions.setMinHeightPosCall(npcId, 13.37f) }
                }
            }

            describe("getter") {
                beforeEach {
                    every { fcnpcNativeFunctions.getMinHeightPosCall(npcId) } returns 0.815f
                }

                it("should return min height callback threshold") {
                    assertThat(npc.minHeightCallbackThreshold)
                            .isEqualTo(0.815f)
                }
            }
        }

        describe("spawn") {
            beforeEach {
                every { fcnpcNativeFunctions.spawn(any(), any(), any(), any(), any()) } returns true
                npc.spawn(SkinModel.RYDER, vector3DOf(1f, 2f, 3f))
            }

            it("should call fcnpcNativeFunctions.spawn") {
                verify {
                    fcnpcNativeFunctions.spawn(npcid = npcId, skinid = SkinModel.RYDER.value, x = 1f, y = 2f, z = 3f)
                }
            }
        }

        describe("respawn") {
            beforeEach {
                every { fcnpcNativeFunctions.respawn(any()) } returns true
                npc.respawn()
            }

            it("should call fcnpcNativeFunctions.respawn") {
                verify { fcnpcNativeFunctions.respawn(npcId) }
            }
        }

        describe("kill") {
            beforeEach {
                every { fcnpcNativeFunctions.kill(any()) } returns true
                npc.kill()
            }

            it("should call fcnpcNativeFunctions.kill") {
                verify { fcnpcNativeFunctions.kill(npcId) }
            }
        }

        describe("isStreamedInForPlayer") {
            val playerId = 69
            val player by memoized {
                mockk<Player> {
                    every { id } returns PlayerId.valueOf(playerId)
                }
            }

            listOf(true, false).forEach { isStreamedIn ->
                context("fcnpcNativeFunctions.isStreamedIn returns $isStreamedIn") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isStreamedIn(npcId, playerId) } returns isStreamedIn
                    }

                    it("should return $isStreamedIn") {
                        assertThat(npc.isStreamedInForPlayer(player))
                                .isEqualTo(isStreamedIn)
                    }
                }
            }
        }

        describe("giveQuaternion") {
            beforeEach {
                every { fcnpcNativeFunctions.giveQuaternion(any(), any(), any(), any(), any()) } returns true
                npc.giveQuaternion(quaternionOf(x = 1f, y = 2f, z = 3f, w = 4f))
            }

            it("should call fcnpcNativeFunctions.giveQuaternion") {
                verify { fcnpcNativeFunctions.giveQuaternion(npcid = npcId, x = 1f, y = 2f, z = 3f, w = 4f) }
            }
        }

        describe("setVelocity") {
            beforeEach {
                every { fcnpcNativeFunctions.setVelocity(any(), any(), any(), any(), any()) } returns true
            }

            listOf(true, false).forEach { updateCoordinates ->
                context("updateCoordinates is set to $updateCoordinates") {
                    beforeEach {
                        npc.setVelocity(vector3DOf(1f, 2f, 3f), updateCoordinates)
                    }

                    it("should call fcnpcNativeFunctions.setVelocity") {
                        verify {
                            fcnpcNativeFunctions.setVelocity(
                                    npcid = npcId,
                                    x = 1f,
                                    y = 2f,
                                    z = 3f,
                                    update_pos = updateCoordinates
                            )
                        }
                    }
                }
            }

            context("updateCoordinates is not set") {
                beforeEach {
                    npc.setVelocity(vector3DOf(1f, 2f, 3f))
                }

                it("should call fcnpcNativeFunctions.setVelocity with update_pos set to false") {
                    verify {
                        fcnpcNativeFunctions.setVelocity(
                                npcid = npcId,
                                x = 1f,
                                y = 2f,
                                z = 3f,
                                update_pos = false
                        )
                    }
                }
            }
        }

        describe("giveVelocity") {
            beforeEach {
                every { fcnpcNativeFunctions.giveVelocity(any(), any(), any(), any(), any()) } returns true
            }

            listOf(true, false).forEach { updateCoordinates ->
                context("updateCoordinates is set to $updateCoordinates") {
                    beforeEach {
                        npc.giveVelocity(vector3DOf(1f, 2f, 3f), updateCoordinates)
                    }

                    it("should call fcnpcNativeFunctions.giveVelocity") {
                        verify {
                            fcnpcNativeFunctions.giveVelocity(
                                    npcid = npcId,
                                    x = 1f,
                                    y = 2f,
                                    z = 3f,
                                    update_pos = updateCoordinates
                            )
                        }
                    }
                }
            }

            context("updateCoordinates is not set") {
                beforeEach {
                    npc.giveVelocity(vector3DOf(1f, 2f, 3f))
                }

                it("should call fcnpcNativeFunctions.giveVelocity with update_pos set to false") {
                    verify {
                        fcnpcNativeFunctions.giveVelocity(
                                npcid = npcId,
                                x = 1f,
                                y = 2f,
                                z = 3f,
                                update_pos = false
                        )
                    }
                }
            }
        }

        describe("giveHealth") {
            beforeEach {
                every { fcnpcNativeFunctions.giveHealth(npcId, 13.37f) } returns 69f
            }

            it("should return updated health") {
                assertThat(npc.giveHealth(13.37f))
                        .isEqualTo(69f)
            }
        }

        describe("giveArmour") {
            beforeEach {
                every { fcnpcNativeFunctions.giveArmour(npcId, 13.37f) } returns 69f
            }

            it("should return updated armour") {
                assertThat(npc.giveArmour(13.37f))
                        .isEqualTo(69f)
            }
        }

        describe("giveCoordinates") {
            beforeEach {
                every { fcnpcNativeFunctions.givePosition(any(), any(), any(), any()) } returns true
                npc.giveCoordinates(vector3DOf(1f, 2f, 3f))
            }

            it("should call fcnpcNativeFunctions.givePosition") {
                verify { fcnpcNativeFunctions.givePosition(npcId, 1f, 2f, 3f) }
            }
        }

        describe("giveAngle") {
            beforeEach {
                every { fcnpcNativeFunctions.giveAngle(npcId, 0.815f) } returns 13.37f
            }

            it("should return new angle") {
                assertThat(npc.giveAngle(0.815f))
                        .isEqualTo(13.37f)
            }
        }

        describe("setAngleTo") {
            context("target is coordinates") {
                beforeEach {
                    every { fcnpcNativeFunctions.setAngleToPos(any(), any(), any()) } returns true
                    npc.setAngleTo(vector2DOf(1f, 2f))
                }

                it("should call fcnpcNativeFunctions.setAngleToPos") {
                    verify { fcnpcNativeFunctions.setAngleToPos(npcId, 1f, 2f) }
                }
            }

            context("target is player") {
                val playerId = 1234
                val player by memoized {
                    mockk<Player> {
                        every { id } returns PlayerId.valueOf(playerId)
                    }
                }

                beforeEach {
                    every { fcnpcNativeFunctions.setAngleToPlayer(any(), any()) } returns true
                    npc.setAngleTo(player)
                }

                it("should call fcnpcNativeFunctions.setAngleToPlayer") {
                    verify { fcnpcNativeFunctions.setAngleToPlayer(npcId, playerId) }
                }
            }
        }

        describe("stopPlayingNode") {
            beforeEach {
                every { fcnpcNativeFunctions.stopPlayingNode(any()) } returns true
                npc.stopPlayingNode()
            }

            it("should call fcnpcNativeFunctions.stopPlayingNode") {
                verify { fcnpcNativeFunctions.stopPlayingNode(npcId) }
            }
        }

        describe("pausePlayingNode") {
            beforeEach {
                every { fcnpcNativeFunctions.pausePlayingNode(any()) } returns true
                npc.pausePlayingNode()
            }

            it("should call fcnpcNativeFunctions.pausePlayingNode") {
                verify { fcnpcNativeFunctions.pausePlayingNode(npcId) }
            }
        }

        describe("resumePlayingNode") {
            beforeEach {
                every { fcnpcNativeFunctions.resumePlayingNode(any()) } returns true
                npc.resumePlayingNode()
            }

            it("should call fcnpcNativeFunctions.resumePlayingNode") {
                verify { fcnpcNativeFunctions.resumePlayingNode(npcId) }
            }
        }

        describe("showInTabListForPlayer") {
            val playerId = 1337
            val player by memoized {
                mockk<Player> {
                    every { id } returns PlayerId.valueOf(playerId)
                }
            }

            beforeEach {
                every { fcnpcNativeFunctions.showInTabListForPlayer(any(), any()) } returns true
                npc.showInTabListForPlayer(player)
            }

            it("should call fcnpcNativeFunctions.showInTabListForPlayer") {
                verify { fcnpcNativeFunctions.showInTabListForPlayer(npcid = npcId, forplayerid = playerId) }
            }
        }

        describe("hideInTabListForPlayer") {
            val playerId = 1337
            val player by memoized {
                mockk<Player> {
                    every { id } returns PlayerId.valueOf(playerId)
                }
            }

            beforeEach {
                every { fcnpcNativeFunctions.hideInTabListForPlayer(any(), any()) } returns true
                npc.hideInTabListForPlayer(player)
            }

            it("should call fcnpcNativeFunctions.hideInTabListForPlayer") {
                verify { fcnpcNativeFunctions.hideInTabListForPlayer(npcid = npcId, forplayerid = playerId) }
            }
        }

        describe("goByMovePath") {
            getGoByMovePathParameters().forEach { parameters ->
                context("parameters are set to $parameters") {
                    val movePathId = 1337
                    val movePathPointId = 69
                    val movePathPoint by memoized {
                        val movePath = mockk<MovePath> {
                            every { id } returns MovePathId.valueOf(movePathId)
                        }
                        mockk<MovePathPoint> {
                            every { id } returns MovePathPointId.valueOf(movePathPointId)
                            every { this@mockk.movePath } returns movePath
                        }
                    }

                    beforeEach {
                        every {
                            fcnpcNativeFunctions.goByMovePath(
                                    any(),
                                    any(),
                                    any(),
                                    any(),
                                    any(),
                                    any(),
                                    any(),
                                    any(),
                                    any(),
                                    any()
                            )
                        } returns true
                        npc.goByMovePath(movePathPoint, parameters)
                    }

                    it("should call fcnpcNativeFunctions.goByMovePath") {
                        verify {
                            fcnpcNativeFunctions.goByMovePath(
                                    npcid = npcId,
                                    pathid = movePathId,
                                    pointid = movePathPointId,
                                    type = parameters.type.value,
                                    speed = parameters.speed.value,
                                    mode = parameters.mode.value,
                                    pathfinding = parameters.pathFinding.value,
                                    radius = parameters.radius,
                                    set_angle = parameters.setAngle,
                                    min_distance = parameters.minDistance
                            )
                        }
                    }
                }
            }
        }

        describe("destroy") {
            beforeEach {
                every { fcnpcNativeFunctions.destroy(any()) } returns true
                npc.destroy()
            }

            it("should destroy extension") {
                assertThat(npc.extensions.isDestroyed)
                        .isTrue()
            }

            it("should call fcnpcNativeFunctions.destroy") {
                verify { fcnpcNativeFunctions.destroy(npcId) }
            }
        }
    }
}) {

    fun getGoByMovePathParameters(): List<GoByMovePathParameters> {
        return Stream
                .of(
                        Arrays.stream(MoveType.values()).map { GoByMovePathParameters().copy(type = it) },
                        Arrays.stream(MoveSpeed.values()).map { GoByMovePathParameters().copy(speed = it) },
                        Arrays.stream(MoveMode.values()).map { GoByMovePathParameters().copy(mode = it) },
                        Arrays.stream(MovePathFinding.values()).map { GoByMovePathParameters().copy(pathFinding = it) },
                        Stream.of(true, false).map { GoByMovePathParameters().copy(setAngle = it) }
                )
                .flatMap(Function.identity())
                .toList()
    }
}