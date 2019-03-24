package ch.leadrian.samp.kamp.fcnpcwrapper.data

import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveSpeed
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveType

data class PlayNodeParameters
@JvmOverloads
constructor(
        val moveType: MoveType = MoveType.AUTO,
        val speed: MoveSpeed = MoveSpeed.AUTO,
        val mode: MoveMode = MoveMode.AUTO,
        val radius: Float = 0f,
        val setAngle: Boolean = true
) {

    companion object {

        val DEFAULT = PlayNodeParameters()

    }

}