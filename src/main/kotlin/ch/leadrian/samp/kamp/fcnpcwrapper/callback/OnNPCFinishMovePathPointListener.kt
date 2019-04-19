package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePathPoint

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnNPCFinishMovePathPointListener {

    @IgnoredReturnValue(Result.Continue::class)
    fun onNPCFinishMovePathPoint(npc: FullyControllableNPC, movePathPoint: MovePathPoint): Result

    sealed class Result(val value: Boolean) {

        object Continue : Result(true)

        object Stop : Result(false)
    }

}