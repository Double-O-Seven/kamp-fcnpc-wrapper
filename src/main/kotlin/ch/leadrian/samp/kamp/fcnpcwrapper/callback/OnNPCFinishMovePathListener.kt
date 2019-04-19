package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePath

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.fcnpcwrapper.callback")
interface OnNPCFinishMovePathListener {

    fun onNPCFinishMovePath(npc: FullyControllableNPC, movePath: MovePath)

}