package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
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

        describe("destroy") {
            beforeEach {
                every { fcnpcNativeFunctions.destroy(any()) } returns true
                npc.destroy()
            }

            it("should call fcnpcNativeFunctions.destroy") {
                verify { fcnpcNativeFunctions.destroy(npcId) }
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

        describe("isSpawn") {
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

        describe("kill") {
            beforeEach {
                every { fcnpcNativeFunctions.kill(any()) } returns true
                npc.kill()
            }

            it("should call fcnpcNativeFunctions.kill") {
                verify { fcnpcNativeFunctions.kill(npcId) }
            }
        }
    }

})