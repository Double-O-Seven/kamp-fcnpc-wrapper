package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePath
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.MovePathRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object MovePathFactorySpec : Spek({
    val movePathRegistry by memoized { mockk<MovePathRegistry>() }
    val nativeFunctions by memoized { mockk<FCNPCNativeFunctions>(relaxed = true) }
    val movePathFactory by memoized { MovePathFactory(movePathRegistry, nativeFunctions) }

    describe("create") {
        lateinit var movePath: MovePath

        beforeEach {
            every { movePathRegistry.register(any()) } just Runs
            movePath = movePathFactory.create()
        }

        it("should register move path in registry") {
            verify { movePathRegistry.register(movePath) }
        }

        context("movePath.onDestroy is called") {
            beforeEach {
                every { movePathRegistry.unregister(any()) } just Runs
                movePath.destroy()
            }

            it("should unregister move path in registry") {
                verify { movePathRegistry.unregister(movePath) }
            }
        }
    }

})