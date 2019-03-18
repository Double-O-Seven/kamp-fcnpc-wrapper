package ch.leadrian.samp.kamp.fcnpcwrapper.data

import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MovePathFinding
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveSpeed
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveType

data class GoToParameters(
        val type: MoveType = MoveType.AUTO,
        val speed: MoveSpeed = MoveSpeed.AUTO,
        val mode: MoveMode = MoveMode.AUTO,
        val pathFinding: MovePathFinding = MovePathFinding.AUTO,
        val radius: Float = 0f,
        val setAngle: Boolean = true,
        val minDistance: Float = 0f,
        val distanceCheck: Float = 1.5f,
        val stopDelay: Int = 250
) {

    companion object {

        val DEFAULT = GoToParameters()

    }

}