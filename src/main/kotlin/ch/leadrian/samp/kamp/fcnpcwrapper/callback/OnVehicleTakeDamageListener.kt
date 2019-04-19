package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnVehicleTakeDamageListener {

    @IgnoredReturnValue(Result.Sync::class)
    fun onVehicleTakeDamage(
            npc: FullyControllableNPC,
            vehicle: Vehicle,
            issuer: Player?,
            amount: Float,
            weapon: WeaponModel,
            coordinates: Vector3D
    ): Result

    sealed class Result(val value: Boolean) {

        object Sync : Result(true)

        object Desync : Result(false)
    }

}