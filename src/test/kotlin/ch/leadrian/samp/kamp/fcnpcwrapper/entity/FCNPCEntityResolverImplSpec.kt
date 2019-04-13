package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.FullyControllableNPCRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class FCNPCEntityResolverImplSpec : Spek({
    val fullyControllableNPCRegistry by memoized { mockk<FullyControllableNPCRegistry>() }
    val fcnpcEntityResolver by memoized {
        FCNPCEntityResolverImpl(fullyControllableNPCRegistry, mockk())
    }

    describe("toNPC") {
        val npcId = 1337

        context("npc ID is valid") {
            val expectedNPC = mockk<FullyControllableNPC>()
            lateinit var npc: FullyControllableNPC

            beforeEach {
                every { fullyControllableNPCRegistry[npcId] } returns expectedNPC
                npc = fcnpcEntityResolver.run { npcId.toNPC() }
            }

            it("should return NPC") {
                assertThat(npc)
                        .isEqualTo(expectedNPC)
            }
        }

        context("npc ID is not valid") {
            lateinit var caughtThrowable: Throwable

            beforeEach {
                every { fullyControllableNPCRegistry[npcId] } returns null
                caughtThrowable = catchThrowable { fcnpcEntityResolver.run { npcId.toNPC() } }
            }

            it("should throw exception") {
                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid NPC ID 1337")
            }
        }
    }
})