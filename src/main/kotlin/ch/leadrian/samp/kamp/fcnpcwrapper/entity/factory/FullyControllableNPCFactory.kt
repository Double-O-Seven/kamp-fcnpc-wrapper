package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.FullyControllableNPCRegistry
import javax.inject.Inject

internal class FullyControllableNPCFactory
@Inject
constructor(
        private val nativeFunctions: FCNPCNativeFunctions,
        private val playerService: PlayerService,
        private val combatFactory: FCNPCCombatFactory,
        private val vehicleFactory: FCNPCVehicleFactory,
        private val surfingFactory: FCNPCSurfingFactory,
        private val fullyControllableNPCRegistry: FullyControllableNPCRegistry
) {

    fun create(name: String): FullyControllableNPC {
        val npc = FullyControllableNPC(
                name,
                nativeFunctions,
                playerService,
                combatFactory,
                vehicleFactory,
                surfingFactory
        )
        fullyControllableNPCRegistry.register(npc)
        npc.onDestroy {
            fullyControllableNPCRegistry.unregister(this)
        }
        return npc
    }

}