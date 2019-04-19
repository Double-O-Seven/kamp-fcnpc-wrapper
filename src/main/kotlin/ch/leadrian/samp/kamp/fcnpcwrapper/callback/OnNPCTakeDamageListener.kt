package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnNPCTakeDamageListener {

    @IgnoredReturnValue(Result.Allow::class)
    fun onNPCTakeDamage(
            npc: FullyControllableNPC,
            issuer: Player?,
            amount: Float,
            weaponModel: WeaponModel,
            bodyPart: BodyPart
    ): Result

    sealed class Result(val value: Boolean) {

        object Allow : Result(true)

        object Prevent : Result(false)
    }

}