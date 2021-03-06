package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object PlaybackRecordRegistrySpec : Spek({

    describe("capacity") {
        val playbackRecordRegistry by memoized {
            PlaybackRecordRegistry(50)
        }

        it("should be expected value") {
            assertThat(playbackRecordRegistry.capacity)
                    .isEqualTo(50)
        }
    }

})