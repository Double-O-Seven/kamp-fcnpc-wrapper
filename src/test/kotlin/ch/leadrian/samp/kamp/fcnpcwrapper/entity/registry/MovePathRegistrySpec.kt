package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object MovePathRegistrySpec : Spek({

    describe("capacity") {
        val movePathRegistry by memoized {
            MovePathRegistry(65536)
        }

        it("should be expected value") {
            assertThat(movePathRegistry.capacity)
                    .isEqualTo(65536)
        }
    }

})