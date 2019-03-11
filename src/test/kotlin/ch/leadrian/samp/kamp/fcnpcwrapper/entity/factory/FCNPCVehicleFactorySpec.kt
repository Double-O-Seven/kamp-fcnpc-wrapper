package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCVehicle
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FCNPCVehicleFactorySpec : Spek({
    val fcnpcVehicleFactory by memoized {
        FCNPCVehicleFactory(mockk(), mockk())
    }

    describe("create") {
        val npc by memoized { mockk<FullyControllableNPC>() }
        lateinit var vehicle: FCNPCVehicle

        beforeEach {
            vehicle = fcnpcVehicleFactory.create(npc)
        }

        it("should create vehicle with NPC") {
            assertThat(vehicle.npc)
                    .isEqualTo(npc)
        }
    }
})