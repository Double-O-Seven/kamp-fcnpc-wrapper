package ch.leadrian.samp.kamp.fcnpcwrapper.data

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.EntityCheck
import java.util.EnumSet

data class WeaponShotParameters
@JvmOverloads
constructor(
        val weapon: WeaponModel,
        val hitTarget: OnPlayerWeaponShotListener.Target,
        val coordinates: Vector3D,
        val isHit: Boolean = true,
        val offsetFrom: Vector3D = Vector3D.ORIGIN,
        val betweenChecks: Set<EntityCheck> = EnumSet.of(EntityCheck.ALL)
) {

    internal val betweenChecksValue: Int
        get() = betweenChecks.fold(0) { value, check -> value or check.value }

}
