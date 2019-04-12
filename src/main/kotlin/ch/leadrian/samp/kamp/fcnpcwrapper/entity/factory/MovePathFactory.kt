package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePath
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.MovePathRegistry
import javax.inject.Inject

internal class MovePathFactory
@Inject
constructor(
        private val movePathRegistry: MovePathRegistry,
        private val nativeFunctions: FCNPCNativeFunctions
) {

    fun create(): MovePath {
        val movePath = MovePath(nativeFunctions)
        movePathRegistry.register(movePath)
        movePath.onDestroy {
            movePathRegistry.unregister(this)
        }
        return movePath
    }

}