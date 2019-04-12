package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.FullyControllableNPCRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FullyControllableNPCFactorySpec : Spek({
    val fullyControllableNPCRegistry by memoized { mockk<FullyControllableNPCRegistry>() }
    val nativeFunctions by memoized { mockk<FCNPCNativeFunctions>(relaxed = true) }
    val fullyControllableNPCFactory by memoized {
        FullyControllableNPCFactory(nativeFunctions, mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true), fullyControllableNPCRegistry)
    }

    describe("create") {
        lateinit var fullyControllableNPC: FullyControllableNPC

        beforeEach {
            every { fullyControllableNPCRegistry.register(any()) } just Runs
            fullyControllableNPC = fullyControllableNPCFactory.create("Hans_Wurst")
        }

        it("should register move path in registry") {
            verify { fullyControllableNPCRegistry.register(fullyControllableNPC) }
        }

        it("should create NPC with name") {
            verify { nativeFunctions.create("Hans_Wurst") }
        }

        context("fullyControllableNPC.onDestroy is called") {
            beforeEach {
                every { fullyControllableNPCRegistry.unregister(any()) } just Runs
                fullyControllableNPC.destroy()
            }

            it("should unregister move path in registry") {
                verify { fullyControllableNPCRegistry.unregister(fullyControllableNPC) }
            }
        }
    }

})