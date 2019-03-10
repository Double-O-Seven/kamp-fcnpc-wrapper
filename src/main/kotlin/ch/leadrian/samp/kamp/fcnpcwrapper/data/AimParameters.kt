package ch.leadrian.samp.kamp.fcnpcwrapper.data

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.EntityCheck
import java.util.EnumSet

data class AimParameters(
        val shoot: Boolean = false,
        val shootDelay: Int? = null,
        val setAngle: Boolean = true,
        val offsetFrom: Vector3D = Vector3D.ORIGIN,
        val betweenChecks: Set<EntityCheck> = EnumSet.of(EntityCheck.ALL)
) {

    companion object {

        val DEFAULT = AimParameters()

    }

    internal val betweenChecksValue: Int
        get() = betweenChecks.fold(0) { value, check -> value or check.value }

}