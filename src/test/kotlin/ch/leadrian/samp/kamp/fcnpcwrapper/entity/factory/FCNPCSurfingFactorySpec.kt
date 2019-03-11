package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCSurfing
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FCNPCSurfingFactorySpec : Spek({
    val fcnpcSurfingFactory by memoized {
        FCNPCSurfingFactory(mockk(), mockk(), mockk(), mockk(), mockk())
    }

    describe("create") {
        val npc by memoized { mockk<FullyControllableNPC>() }
        lateinit var surfing: FCNPCSurfing

        beforeEach {
            surfing = fcnpcSurfingFactory.create(npc)
        }

        it("should create surfing with NPC") {
            Assertions.assertThat(surfing.npc)
                    .isEqualTo(npc)
        }
    }
})