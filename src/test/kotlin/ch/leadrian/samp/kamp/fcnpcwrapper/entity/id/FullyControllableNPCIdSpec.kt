package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FullyControllableNPCIdSpec : Spek({
    describe("valueOf") {
        listOf(-1, 0, 999, 1000, SAMPConstants.INVALID_PLAYER_ID).forEach { value ->
            it("should return FullyControllableNPCId with value $value") {
                val fullyControllableNPCId = FullyControllableNPCId.valueOf(value)

                assertThat(fullyControllableNPCId.value)
                        .isEqualTo(value)
            }
        }

        listOf(0, 500, 999, SAMPConstants.INVALID_PLAYER_ID).forEach { value ->
            it("should used cached FullyControllableNPCId for value $value") {
                val fullyControllableNPCId = FullyControllableNPCId.valueOf(value)

                assertThat(fullyControllableNPCId)
                        .isSameAs(FullyControllableNPCId.valueOf(value))
            }
        }

        listOf(-1, 1000, 1001).forEach { value ->
            it("should create new FullyControllableNPCId for value $value") {
                val fullyControllableNPCId = FullyControllableNPCId.valueOf(value)

                assertThat(fullyControllableNPCId)
                        .isNotSameAs(FullyControllableNPCId.valueOf(value))
            }
        }
    }

    describe("toPlayerId") {
        val fullyControllableNPCId = FullyControllableNPCId.valueOf(1337)

        it("should convert to player ID") {
            assertThat(fullyControllableNPCId.toPlayerId())
                    .isEqualTo(PlayerId.valueOf(1337))
        }
    }

    describe("PlayerId.toFullyControllableNPCId") {
        val playerId = PlayerId.valueOf(1337)

        it("should convert to NPC ID") {
            assertThat(playerId.toFullyControllableNPCId())
                    .isEqualTo(FullyControllableNPCId.valueOf(1337))
        }
    }
})