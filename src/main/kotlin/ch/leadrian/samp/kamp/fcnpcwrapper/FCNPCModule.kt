package ch.leadrian.samp.kamp.fcnpcwrapper

import ch.leadrian.samp.kamp.core.api.inject.KampModule

class FCNPCModule : KampModule() {

    override fun configure() {
        bind(FCNPCCallbackManager::class.java).asEagerSingleton()
    }

}