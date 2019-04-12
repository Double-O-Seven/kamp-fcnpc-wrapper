package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import ch.leadrian.samp.kamp.core.api.service.PlayerService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FullyControllableNPCRegistrySpec : Spek({

    val playerService by memoized { mockk<PlayerService>() }
    val fullyControllableNPCRegistry by memoized { FullyControllableNPCRegistry(playerService) }

    describe("capacity") {
        val maxPlayers = 1234

        beforeEach {
            every { playerService.getMaxPlayers() } returns maxPlayers
        }

        it("should return max players") {
            assertThat(fullyControllableNPCRegistry.capacity)
                    .isEqualTo(maxPlayers)
        }
    }

})