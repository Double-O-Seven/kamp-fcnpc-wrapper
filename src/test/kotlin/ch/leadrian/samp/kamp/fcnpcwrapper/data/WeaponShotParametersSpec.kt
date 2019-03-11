package ch.leadrian.samp.kamp.fcnpcwrapper.data

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.PlayerHitTarget
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.EntityCheck
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.EnumSet

internal object WeaponShotParametersSpec : Spek({
    describe("betweenCheckValue") {
        context("multiple values are set") {
            val weaponShotParameters by memoized {
                WeaponShotParameters(
                        weapon = WeaponModel.M4,
                        hitTarget = PlayerHitTarget(mockk()),
                        coordinates = vector3DOf(1f, 2f, 3f),
                        betweenChecks = EnumSet.of(EntityCheck.PLAYER, EntityCheck.NPC)
                )
            }

            it("should have EntityCheck.PLAYER set") {
                assertThat(weaponShotParameters.betweenChecksValue and EntityCheck.PLAYER.value)
                        .isNotZero()
            }

            it("should have EntityCheck.NPC set") {
                assertThat(weaponShotParameters.betweenChecksValue and EntityCheck.NPC.value)
                        .isNotZero()
            }

            it("should not have EntityCheck.OBJECT set") {
                assertThat(weaponShotParameters.betweenChecksValue and EntityCheck.OBJECT.value)
                        .isZero()
            }
        }
    }
})