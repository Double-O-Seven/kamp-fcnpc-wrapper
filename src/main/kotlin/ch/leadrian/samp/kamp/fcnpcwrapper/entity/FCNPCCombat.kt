package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.constants.FightingStyle
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.data.AimParameters

class FCNPCCombat
internal constructor(
        override val npc: FullyControllableNPC,
        private val nativeFunctions: FCNPCNativeFunctions,
        private val playerService: PlayerService
) : HasFullyControllableNPC {

    var fightingStyle: FightingStyle
        get() = FightingStyle[nativeFunctions.getFightingStyle(npc.id.value)]
        set(value) {
            nativeFunctions.setFightingStyle(npcid = npc.id.value, style = value.value)
        }

    var isReloadingUsed: Boolean
        get() = nativeFunctions.isReloadingUsed(npc.id.value)
        set(value) {
            nativeFunctions.useReloading(npc.id.value, value)
        }

    var isInfiniteAmmoUsed: Boolean
        get() = nativeFunctions.isInfiniteAmmoUsed(npc.id.value)
        set(value) {
            nativeFunctions.useInfiniteAmmo(npc.id.value, value)
        }

    val isAttacking: Boolean
        get() = nativeFunctions.isAttacking(npc.id.value)

    val isAiming: Boolean
        get() = nativeFunctions.isAiming(npc.id.value)

    val aimingPlayer: Player?
        get() {
            val playerId = PlayerId.valueOf(nativeFunctions.getAimingPlayer(npc.id.value))
            return if (playerService.isPlayerConnected(playerId)) {
                playerService.getPlayer(playerId)
            } else {
                null
            }
        }

    val isShooting: Boolean
        get() = nativeFunctions.isShooting(npc.id.value)

    val isReloading: Boolean
        get() = nativeFunctions.isReloading(npc.id.value)

    @JvmOverloads
    fun aimAt(coordinates: Vector3D, parameters: AimParameters = AimParameters.DEFAULT) {
        nativeFunctions.aimAt(
                npcid = npc.id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                shoot = parameters.shoot,
                shoot_delay = parameters.shootDelay ?: -1,
                set_angle = parameters.setAngle,
                offset_from_x = parameters.offsetFrom.x,
                offset_from_y = parameters.offsetFrom.y,
                offset_from_z = parameters.offsetFrom.z,
                between_check_flags = parameters.betweenChecksValue
        )
    }

    @JvmOverloads
    fun aimAt(player: Player, parameters: AimParameters = AimParameters.DEFAULT, offset: Vector3D = Vector3D.ORIGIN) {
        nativeFunctions.aimAtPlayer(
                npcid = npc.id.value,
                playerid = player.id.value,
                offset_x = offset.x,
                offset_y = offset.y,
                offset_z = offset.z,
                shoot = parameters.shoot,
                shoot_delay = parameters.shootDelay ?: -1,
                set_angle = parameters.setAngle,
                offset_from_x = parameters.offsetFrom.x,
                offset_from_y = parameters.offsetFrom.y,
                offset_from_z = parameters.offsetFrom.z,
                between_check_flags = parameters.betweenChecksValue
        )
    }

    fun stopAim() {
        nativeFunctions.stopAim(npc.id.value)
    }

    fun meleeAttack(delay: Int? = null, useFightingStyle: Boolean = false) {
        nativeFunctions.meleeAttack(npcid = npc.id.value, delay = delay ?: -1, fightstyle = useFightingStyle)
    }

    fun stopAttack() {
        nativeFunctions.stopAttack(npc.id.value)
    }

    fun isAimingAt(player: Player): Boolean =
            nativeFunctions.isAimingAtPlayer(npcid = npc.id.value, playerid = player.id.value)

}