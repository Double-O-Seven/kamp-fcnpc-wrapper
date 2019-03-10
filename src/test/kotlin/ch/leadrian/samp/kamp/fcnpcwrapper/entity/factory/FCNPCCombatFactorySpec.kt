package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCCombat
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FCNPCCombatFactorySpec : Spek({
    val fcnpcCombatFactory by memoized {
        FCNPCCombatFactory(mockk(), mockk(), mockk())
    }

    describe("create") {
        val npc by memoized { mockk<FullyControllableNPC>() }
        lateinit var combat: FCNPCCombat

        beforeEach {
            combat = fcnpcCombatFactory.create(npc)
        }

        it("should create combat with NPC") {
            assertThat(combat.npc)
                    .isEqualTo(npc)
        }
    }
})