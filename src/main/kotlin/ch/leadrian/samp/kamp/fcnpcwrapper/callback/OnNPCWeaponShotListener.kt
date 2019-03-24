package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.HitTarget
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnNPCWeaponShotListener {

    @IgnoredReturnValue(Result.AllowDamage::class)
    fun onNPCWeaponShot(
            npc: FullyControllableNPC,
            weaponModel: WeaponModel,
            hitTarget: HitTarget,
            coordinates: Vector3D
    ): Result

    sealed class Result(val value: Boolean) {

        object AllowDamage : Result(true)

        object PreventDamage : Result(false)
    }

}