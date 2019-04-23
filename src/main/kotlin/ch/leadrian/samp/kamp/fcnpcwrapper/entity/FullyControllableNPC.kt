package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.SpecialAction
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.PlayerKeys
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Quaternion
import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.playerKeysOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.quaternionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.core.api.entity.extension.Extendable
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.data.GoByMovePathParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.FCNPCCombatFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.FCNPCSurfingFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory.FCNPCVehicleFactory
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId

class FullyControllableNPC
internal constructor(
        val name: String,
        private val nativeFunctions: FCNPCNativeFunctions,
        private val playerService: PlayerService,
        combatFactory: FCNPCCombatFactory,
        vehicleFactory: FCNPCVehicleFactory,
        surfingFactory: FCNPCSurfingFactory
) : AbstractDestroyable(), Entity<FullyControllableNPCId>, Extendable<FullyControllableNPC> {

    override val id: FullyControllableNPCId
        get() = requireNotDestroyed { field }

    init {
        val npcId = nativeFunctions.create(name)
        if (npcId == SAMPConstants.INVALID_PLAYER_ID) {
            throw CreationFailedException("Could not create NPC")
        }

        id = FullyControllableNPCId(npcId)
    }

    override val extensions: EntityExtensionContainer<FullyControllableNPC> = EntityExtensionContainer(this)

    val weapons: FCNPCWeapons = FCNPCWeapons(this, nativeFunctions)

    val animation: FCNPCAnimation = FCNPCAnimation(this, nativeFunctions)

    val combat: FCNPCCombat = combatFactory.create(this)

    val vehicle: FCNPCVehicle = vehicleFactory.create(this)

    val surfing: FCNPCSurfing = surfingFactory.create(this)

    val playback: Playback = Playback(this, nativeFunctions)

    val movement: Movement = Movement(this, nativeFunctions)

    val isSpawned: Boolean
        get() = nativeFunctions.isSpawned(id.value)

    val isDead: Boolean
        get() = nativeFunctions.isDead(id.value)

    val isStreamedInForAnyone: Boolean
        get() = nativeFunctions.isStreamedInForAnyone(id.value)

    var coordinates: Vector3D
        get() {
            val x = MutableFloatCell()
            val y = MutableFloatCell()
            val z = MutableFloatCell()
            nativeFunctions.getPosition(npcid = id.value, x = x, y = y, z = z)
            return vector3DOf(x.value, y.value, z.value)
        }
        set(value) {
            nativeFunctions.setPosition(npcid = id.value, x = value.x, y = value.y, z = value.z)
        }

    var angle: Float
        get() = nativeFunctions.getAngle(id.value)
        set(value) {
            nativeFunctions.setAngle(id.value, value)
        }

    var quaternion: Quaternion
        get() {
            val x = MutableFloatCell()
            val y = MutableFloatCell()
            val z = MutableFloatCell()
            val w = MutableFloatCell()
            nativeFunctions.getQuaternion(npcid = id.value, w = w, x = x, y = y, z = z)
            return quaternionOf(x = x.value, y = y.value, z = z.value, w = w.value)
        }
        set(value) {
            nativeFunctions.setQuaternion(npcid = id.value, x = value.x, y = value.y, z = value.z, w = value.w)
        }

    val velocity: Vector3D
        get() {
            val x = MutableFloatCell()
            val y = MutableFloatCell()
            val z = MutableFloatCell()
            nativeFunctions.getVelocity(npcid = id.value, x = x, y = y, z = z)
            return vector3DOf(x.value, y.value, z.value)
        }

    var speed: Float
        get() = nativeFunctions.getSpeed(id.value)
        set(value) {
            nativeFunctions.setSpeed(id.value, value)
        }

    var interiorId: Int
        get() = nativeFunctions.getInterior(id.value)
        set(value) {
            nativeFunctions.setInterior(npcid = id.value, interiorid = value)
        }

    var virtualWorldId: Int
        get() = nativeFunctions.getVirtualWorld(id.value)
        set(value) {
            nativeFunctions.setVirtualWorld(npcid = id.value, worldid = value)
        }

    var position: Position
        get() = positionOf(coordinates, angle)
        set(value) {
            coordinates = value
            angle = value.angle
        }

    var location: Location
        get() = locationOf(coordinates = coordinates, interiorId = interiorId, worldId = virtualWorldId)
        set(value) {
            coordinates = value
            interiorId = value.interiorId
            virtualWorldId = value.virtualWorldId
        }

    var angledLocation: AngledLocation
        get() = angledLocationOf(location = location, angle = angle)
        set(value) {
            location = value
            angle = value.angle
        }

    var health: Float
        get() = nativeFunctions.getHealth(id.value)
        set(value) {
            nativeFunctions.setHealth(id.value, value)
        }

    var armour: Float
        get() = nativeFunctions.getArmour(id.value)
        set(value) {
            nativeFunctions.setArmour(id.value, value)
        }

    var isInvulnerable: Boolean
        get() = nativeFunctions.isInvulnerable(id.value)
        set(value) {
            nativeFunctions.setInvulnerable(id.value, value)
        }

    var skinModel: SkinModel
        get() = SkinModel[nativeFunctions.getSkin(id.value)]
        set(value) {
            nativeFunctions.setSkin(npcid = id.value, skinid = value.value)
        }

    var keys: PlayerKeys
        get() {
            val keys = MutableIntCell()
            val upDown = MutableIntCell()
            val leftRight = MutableIntCell()
            nativeFunctions.getKeys(npcid = id.value, ud_analog = upDown, lr_analog = leftRight, keys = keys)
            return playerKeysOf(keys = keys.value, upDown = upDown.value, leftRight = leftRight.value)
        }
        set(value) {
            nativeFunctions.setKeys(
                    npcid = id.value,
                    ud_analog = value.upDown,
                    lr_analog = value.leftRight,
                    keys = value.keys
            )
        }

    var specialAction: SpecialAction
        get() = SpecialAction[nativeFunctions.getSpecialAction(id.value)]
        set(value) {
            nativeFunctions.setSpecialAction(npcid = id.value, actionid = value.value)
        }

    val isPlayingNode: Boolean
        get() = nativeFunctions.isPlayingNode(id.value)

    val isPlayingNodePaused: Boolean
        get() = nativeFunctions.isPlayingNodePaused(id.value)

    var moveMode: MoveMode
        get() = MoveMode[nativeFunctions.getMoveMode(id.value)]
        set(value) {
            nativeFunctions.setMoveMode(npcid = id.value, mode = value.value)
        }

    var minHeightCallbackThreshold: Float
        get() = nativeFunctions.getMinHeightPosCall(id.value)
        set(value) {
            nativeFunctions.setMinHeightPosCall(id.value, value)
        }

    fun spawn(skinModel: SkinModel, coordinates: Vector3D) {
        nativeFunctions.spawn(
                npcid = id.value,
                skinid = skinModel.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z
        )
    }

    fun respawn() {
        nativeFunctions.respawn(id.value)
    }

    fun kill() {
        nativeFunctions.kill(id.value)
    }

    fun isStreamedInForPlayer(player: Player): Boolean =
            nativeFunctions.isStreamedIn(npcid = id.value, forplayerid = player.id.value)

    fun giveQuaternion(quaternion: Quaternion) {
        nativeFunctions.giveQuaternion(
                npcid = id.value,
                x = quaternion.x,
                y = quaternion.y,
                z = quaternion.z,
                w = quaternion.w
        )
    }

    @JvmOverloads
    fun setVelocity(velocity: Vector3D, updateCoordinates: Boolean = false) {
        nativeFunctions.setVelocity(
                npcid = id.value,
                x = velocity.x,
                y = velocity.y,
                z = velocity.z,
                update_pos = updateCoordinates
        )
    }

    @JvmOverloads
    fun giveVelocity(velocity: Vector3D, updateCoordinates: Boolean = false) {
        nativeFunctions.giveVelocity(
                npcid = id.value,
                x = velocity.x,
                y = velocity.y,
                z = velocity.z,
                update_pos = updateCoordinates
        )
    }

    fun giveHealth(health: Float): Float = nativeFunctions.giveHealth(id.value, health)

    fun giveArmour(armour: Float): Float = nativeFunctions.giveArmour(id.value, armour)

    fun giveCoordinates(offset: Vector3D) {
        nativeFunctions.givePosition(id.value, offset.x, offset.y, offset.z)
    }

    fun giveAngle(angle: Float): Float = nativeFunctions.giveAngle(id.value, angle)

    fun setAngleTo(coordinates: Vector2D) {
        nativeFunctions.setAngleToPos(id.value, coordinates.x, coordinates.y)
    }

    fun setAngleTo(player: Player) {
        nativeFunctions.setAngleToPlayer(id.value, player.id.value)
    }

    fun stopPlayingNode() {
        nativeFunctions.stopPlayingNode(id.value)
    }

    fun pausePlayingNode() {
        nativeFunctions.pausePlayingNode(id.value)
    }

    fun resumePlayingNode() {
        nativeFunctions.resumePlayingNode(id.value)
    }

    fun showInTabListForPlayer(player: Player) {
        nativeFunctions.showInTabListForPlayer(npcid = id.value, forplayerid = player.id.value)
    }

    fun hideInTabListForPlayer(player: Player) {
        nativeFunctions.hideInTabListForPlayer(npcid = id.value, forplayerid = player.id.value)
    }

    @JvmOverloads
    fun goByMovePath(
            movePathPoint: MovePathPoint,
            parameters: GoByMovePathParameters = GoByMovePathParameters.DEFAULT
    ) {
        nativeFunctions.goByMovePath(
                npcid = id.value,
                pathid = movePathPoint.movePath.id.value,
                pointid = movePathPoint.id.value,
                type = parameters.type.value,
                speed = parameters.speed.value,
                mode = parameters.mode.value,
                pathfinding = parameters.pathFinding.value,
                radius = parameters.radius,
                set_angle = parameters.setAngle,
                min_distance = parameters.minDistance
        )
    }

    fun toPlayer(): Player = playerService.getPlayer(id.toPlayerId())

    override fun onDestroy() {
        extensions.destroy()
        nativeFunctions.destroy(id.value)
    }
}