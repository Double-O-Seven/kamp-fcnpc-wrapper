package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.quaternionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FullyControllableNPCSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }

    describe("constructor") {
        val name = "JonSnow"

        context("fcnpcNativeFunctions.create returns valid ID") {
            val npcId = 69
            val npc by memoized {
                every { fcnpcNativeFunctions.create(name) } returns npcId
                FullyControllableNPC(name, fcnpcNativeFunctions)
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
                val caughtThrowable = catchThrowable { FullyControllableNPC(name, fcnpcNativeFunctions) }

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
            FullyControllableNPC(name, fcnpcNativeFunctions)
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

        describe("destroy") {
            beforeEach {
                every { fcnpcNativeFunctions.destroy(any()) } returns true
                npc.destroy()
            }

            it("should call fcnpcNativeFunctions.destroy") {
                verify { fcnpcNativeFunctions.destroy(npcId) }
            }
        }
    }

})