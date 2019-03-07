package ch.leadrian.samp.kamp.fcnpcwrapper

import ch.leadrian.samp.kamp.core.api.Plugin
import com.google.inject.Module

class FCNPCPlugin : Plugin() {

    override fun getModules(): List<Module> = listOf(FCNPCModule())

}