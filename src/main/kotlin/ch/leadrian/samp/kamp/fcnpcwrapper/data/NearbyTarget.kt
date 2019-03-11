package ch.leadrian.samp.kamp.fcnpcwrapper.data

import ch.leadrian.samp.kamp.core.api.data.HitTarget
import ch.leadrian.samp.kamp.core.api.data.Vector3D

data class NearbyTarget(
        val target: HitTarget,
        val coordinates: Vector3D
)