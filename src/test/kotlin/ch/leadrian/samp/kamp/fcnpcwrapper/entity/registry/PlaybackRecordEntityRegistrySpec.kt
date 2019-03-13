package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.core.api.service.PlayerService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object PlaybackRecordEntityRegistrySpec : Spek({

    describe("capacity") {
        val playerService by memoized { mockk<PlayerService>() }
        val playbackRecordEntityRegistry by memoized {
            PlaybackRecordEntityRegistry(
                    playerService
            )
        }

        beforeEach {
            every { playerService.getMaxPlayers() } returns 50
        }

        it("should be expected value") {
            assertThat(playbackRecordEntityRegistry.capacity)
                    .isEqualTo(50)
        }
    }

})