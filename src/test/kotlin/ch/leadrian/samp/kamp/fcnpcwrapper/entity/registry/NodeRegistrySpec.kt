package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object NodeRegistrySpec : Spek({

    describe("capacity") {
        val nodeRegistry by memoized {
            NodeRegistry()
        }

        it("should be expected value") {
            assertThat(nodeRegistry.capacity)
                    .isEqualTo(FCNPCConstants.FCNPC_MAX_NODES)
        }
    }

})