package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId

class FullyControllableNPC
internal constructor(
        val name: String,
        private val nativeFunctions: FCNPCNativeFunctions
) : AbstractDestroyable(), Entity<FullyControllableNPCId> {

    override val id: FullyControllableNPCId

    init {
        val npcId = nativeFunctions.create(name)
        if (npcId == SAMPConstants.INVALID_PLAYER_ID) {
            throw CreationFailedException("Could not create NPC")
        }

        id = FullyControllableNPCId(npcId)
    }

    val isSpawned: Boolean
        get() = nativeFunctions.isSpawned(id.value)

    fun spawn(skinModel: SkinModel, coordinates: Vector3D) {
        nativeFunctions.spawn(
                npcid = id.value,
                skinid = skinModel.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z
        )
    }

    fun respawn() {
        nativeFunctions.respawn(id.value)
    }

    fun kill() {
        nativeFunctions.kill(id.value)
    }

    override fun onDestroy() {
        nativeFunctions.destroy(id.value)
    }
}