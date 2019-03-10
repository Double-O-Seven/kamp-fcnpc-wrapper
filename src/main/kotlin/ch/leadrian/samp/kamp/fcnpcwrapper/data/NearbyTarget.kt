package ch.leadrian.samp.kamp.fcnpcwrapper.data

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener
import ch.leadrian.samp.kamp.core.api.data.Vector3D

data class NearbyTarget(
        val target: OnPlayerWeaponShotListener.Target,
        val coordinates: Vector3D
)