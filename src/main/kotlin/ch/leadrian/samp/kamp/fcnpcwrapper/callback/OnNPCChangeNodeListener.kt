package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnNPCChangeNodeListener {

    @IgnoredReturnValue(Result.Continue::class)
    fun onNPCFinishNode(npc: FullyControllableNPC, oldNode: Node, newNode: Node): Result

    sealed class Result(val value: Boolean) {

        object Continue : Result(true)

        object Stop : Result(false)
    }

}