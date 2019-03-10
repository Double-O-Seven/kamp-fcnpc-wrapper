package ch.leadrian.samp.kamp.fcnpcwrapper.data

import ch.leadrian.samp.kamp.fcnpcwrapper.constants.EntityCheck
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.EnumSet

internal object AimParametersSpec : Spek({
    describe("betweenCheckValue") {
        context("multiple values are set") {
            val aimParameters by memoized {
                AimParameters(betweenChecks = EnumSet.of(EntityCheck.PLAYER, EntityCheck.NPC))
            }

            it("should have EntityCheck.PLAYER set") {
                assertThat(aimParameters.betweenChecksValue and EntityCheck.PLAYER.value)
                        .isNotZero()
            }

            it("should have EntityCheck.NPC set") {
                assertThat(aimParameters.betweenChecksValue and EntityCheck.NPC.value)
                        .isNotZero()
            }

            it("should not have EntityCheck.OBJECT set") {
                assertThat(aimParameters.betweenChecksValue and EntityCheck.OBJECT.value)
                        .isZero()
            }
        }
    }
})