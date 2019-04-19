package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnNPCFinishNodePointListener {

    @IgnoredReturnValue(Result.Continue::class)
    fun onNPCFinishNodePoint(npc: FullyControllableNPC, node: Node, point: Int): Result

    sealed class Result(val value: Boolean) {

        object Continue : Result(true)

        object Stop : Result(false)
    }

}