package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCCombat
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.HitTargetResolver
import javax.inject.Inject

internal class FCNPCCombatFactory
@Inject
constructor(
        private val nativeFunctions: FCNPCNativeFunctions,
        private val playerService: PlayerService,
        private val hitTargetResolver: HitTargetResolver
) {

    fun create(npc: FullyControllableNPC): FCNPCCombat =
            FCNPCCombat(npc, nativeFunctions, playerService, hitTargetResolver)

}