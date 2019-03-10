package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableBooleanCell
import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.data.AnimationParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.data.AnimationWithIdParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.data.NamedAnimationParameters

class FCNPCAnimation
internal constructor(
        override val npc: FullyControllableNPC,
        private val nativeFunctions: FCNPCNativeFunctions
) : HasFullyControllableNPC {

    fun set(animation: AnimationWithIdParameters) {
        nativeFunctions.setAnimation(
                npcid = npc.id.value,
                animationid = animation.id,
                fDelta = animation.fDelta,
                loop = animation.loop,
                lockx = animation.lockX,
                locky = animation.lockY,
                freeze = animation.freeze,
                time = animation.time
        )
    }

    fun set(animation: NamedAnimationParameters) {
        nativeFunctions.setAnimationByName(
                npcid = npc.id.value,
                name = animation.name,
                fDelta = animation.fDelta,
                loop = animation.loop,
                lockx = animation.lockX,
                locky = animation.lockY,
                freeze = animation.freeze,
                time = animation.time
        )
    }

    fun reset() {
        nativeFunctions.resetAnimation(npc.id.value)
    }

    fun get(): AnimationWithIdParameters {
        val id = MutableIntCell()
        val fDelta = MutableFloatCell()
        val loop = MutableBooleanCell()
        val lockX = MutableBooleanCell()
        val lockY = MutableBooleanCell()
        val freeze = MutableBooleanCell()
        val time = MutableIntCell()
        nativeFunctions.getAnimation(
                npcid = npc.id.value,
                animationid = id,
                fDelta = fDelta,
                loop = loop,
                lockx = lockX,
                locky = lockY,
                freeze = freeze,
                time = time
        )
        return AnimationWithIdParameters(
                id = id.value,
                fDelta = fDelta.value,
                loop = loop.value,
                lockX = lockX.value,
                lockY = lockY.value,
                freeze = freeze.value,
                time = time.value
        )
    }

    fun apply(animation: Animation, parameters: AnimationParameters) {
        nativeFunctions.applyAnimation(
                npcid = npc.id.value,
                animlib = animation.library,
                animname = animation.animationName,
                fDelta = parameters.fDelta,
                loop = parameters.loop,
                lockx = parameters.lockX,
                locky = parameters.lockY,
                freeze = parameters.freeze,
                time = parameters.time
        )
    }

    fun clear() {
        nativeFunctions.clearAnimations(npc.id.value)
    }

}