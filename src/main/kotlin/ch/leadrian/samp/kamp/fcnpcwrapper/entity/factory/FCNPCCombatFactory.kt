package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCCombat
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import javax.inject.Inject

internal class FCNPCCombatFactory
@Inject
constructor(private val nativeFunctions: FCNPCNativeFunctions, private val playerService: PlayerService) {

    fun create(npc: FullyControllableNPC): FCNPCCombat = FCNPCCombat(npc, nativeFunctions, playerService)

}