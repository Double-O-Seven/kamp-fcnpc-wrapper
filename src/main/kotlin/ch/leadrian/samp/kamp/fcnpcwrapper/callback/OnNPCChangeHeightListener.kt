package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnNPCChangeHeightListener {

    fun onNPCChangeHeight(npc: FullyControllableNPC, oldZ: Float, newZ: Float)

}