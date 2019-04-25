[![Build Status](https://travis-ci.org/Double-O-Seven/kamp-fcnpc-wrapper.svg?branch=master)](https://travis-ci.org/Double-O-Seven/kamp-fcnpc-wrapper)
[![Release Version](https://img.shields.io/maven-central/v/ch.leadrian.samp.kamp/kamp-fcnpc-wrapper.svg?label=release)](http://search.maven.org/#search%7Cga%7C1%7Ckamp-fcnpc-wrapper)

# Kamp FCNPC Wrapper

A wrapper for the native [FCNPC plugin](https://github.com/ziggi/FCNPC).

To create NPCs, simple inject `ch.leadrian.samp.kamp.fcnpcwrapper.service.FCNPCService` in your class:

```kotlin
class MyAmazingService
@Inject
constructor(private val npcService: FCNPCService) {

    fun doSomething() {
        npcService.create("Hans_Wurst")
    }

}

```

A lot of functionality is wrapped in `ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC`.
However, some additional functionality can be found in:
* `ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePath`
* `ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node`
* `ch.leadrian.samp.kamp.fcnpcwrapper.entity.PlaybackRecord`
All the entities mentioned above can be created and accessed using `FCNPCService`.

In order to listen to NPC related callbacks, register your class as a callback listener just like with any other callback provided by Kamp:

```kotlin
@Singleton
class MyAmazingCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnNPCDeathListener, OnNPCUpdateListener {
    
    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this) 
    }
    
    override fun onNPCDeath(npc: FullyControllableNPC, killer: Player?, reason: WeaponModel) {
        println("Oopsie!")
    }
    
    override fun onNPCUpdate(npc: FullyControllableNPC): Result {
        println("Updated NPC ${npc.id}")
        return OnNPCUpdateListener.Result.Sync    
    }
}

```

All available callbacks can be found in the package `ch.leadrian.samp.kamp.fcnpcwrapper.callback`.
