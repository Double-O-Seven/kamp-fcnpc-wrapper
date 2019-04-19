package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponState
import ch.leadrian.samp.kamp.core.api.data.MapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.NoHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerMapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.VehicleHitTarget
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.exception.SafeCaller
import ch.leadrian.samp.kamp.core.api.exception.UncaughtExceptionNotifier
import ch.leadrian.samp.kamp.core.api.exception.tryAndCatch
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCCallbacks
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCEntityResolver
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathPointId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FCNPCCallbackProcessor
@Inject
constructor(
        private val onNPCCreateHandler: OnNPCCreateHandler,
        private val onNPCDestroyHandler: OnNPCDestroyHandler,
        private val onNPCSpawnHandler: OnNPCSpawnHandler,
        private val onNPCRespawnHandler: OnNPCRespawnHandler,
        private val onNPCDeathHandler: OnNPCDeathHandler,
        private val onNPCUpdateHandler: OnNPCUpdateHandler,
        private val onNPCTakeDamageHandler: OnNPCTakeDamageHandler,
        private val onNPCGiveDamageHandler: OnNPCGiveDamageHandler,
        private val onNPCReachDestinationHandler: OnNPCReachDestinationHandler,
        private val onNPCWeaponShotHandler: OnNPCWeaponShotHandler,
        private val onNPCWeaponStateChangeHandler: OnNPCWeaponStateChangeHandler,
        private val onNPCStreamInHandler: OnNPCStreamInHandler,
        private val onNPCStreamOutHandler: OnNPCStreamOutHandler,
        private val onVehicleEntryCompleteHandler: OnVehicleEntryCompleteHandler,
        private val onVehicleExitCompleteHandler: OnVehicleExitCompleteHandler,
        private val onVehicleTakeDamageHandler: OnVehicleTakeDamageHandler,
        private val onNPCFinishPlaybackHandler: OnNPCFinishPlaybackHandler,
        private val onNPCFinishNodeHandler: OnNPCFinishNodeHandler,
        private val onNPCFinishNodePointHandler: OnNPCFinishNodePointHandler,
        private val onNPCChangeNodeHandler: OnNPCChangeNodeHandler,
        private val onNPCFinishMovePathHandler: OnNPCFinishMovePathHandler,
        private val onNPCFinishMovePathPointHandler: OnNPCFinishMovePathPointHandler,
        private val onNPCChangeHeightHandler: OnNPCChangeHeightHandler,
        entityResolver: FCNPCEntityResolver
) : FCNPCCallbacks, SafeCaller, FCNPCEntityResolver by entityResolver {

    override val log = loggerFor<FCNPCCallbackProcessor>()

    @com.google.inject.Inject(optional = true)
    override var uncaughtExceptionNotifier: UncaughtExceptionNotifier? = null

    override fun onCreate(npcid: Int) {
        tryAndCatch {
            onNPCCreateHandler.onNPCCreate(npcid.toNPC())
        }
    }

    override fun onDestroy(npcid: Int) {
        tryAndCatch {
            onNPCDestroyHandler.onNPCDestroy(npcid.toNPC())
        }
    }

    override fun onSpawn(npcid: Int) {
        tryAndCatch {
            onNPCSpawnHandler.onNPCSpawn(npcid.toNPC())
        }
    }

    override fun onRespawn(npcid: Int) {
        tryAndCatch {
            onNPCRespawnHandler.onNPCRespawn(npcid.toNPC())
        }
    }

    override fun onDeath(npcid: Int, killerid: Int, reason: Int) {
        tryAndCatch {
            onNPCDeathHandler.onNPCDeath(npcid.toNPC(), killerid.toPlayerOrNull(), WeaponModel[reason])
        }
    }

    override fun onUpdate(npcid: Int): Boolean {
        val result = tryAndCatch {
            onNPCUpdateHandler.onNPCUpdate(npcid.toNPC())
        } ?: OnNPCUpdateListener.Result.Sync
        return result.value
    }

    override fun onTakeDamage(npcid: Int, issuerid: Int, amount: Float, weaponid: Int, bodypart: Int): Boolean {
        val result = tryAndCatch {
            onNPCTakeDamageHandler.onNPCTakeDamage(
                    npcid.toNPC(),
                    issuerid.toPlayerOrNull(),
                    amount,
                    WeaponModel[weaponid],
                    BodyPart[bodypart]
            )
        } ?: OnNPCTakeDamageListener.Result.Allow
        return result.value
    }

    override fun onGiveDamage(npcid: Int, damagedid: Int, amount: Float, weaponid: Int, bodypart: Int) {
        tryAndCatch {
            onNPCGiveDamageHandler.onNPCGiveDamage(
                    npcid.toNPC(),
                    damagedid.toPlayer(),
                    amount,
                    WeaponModel[weaponid],
                    BodyPart[bodypart]
            )
        }
    }

    override fun onReachDestination(npcid: Int) {
        tryAndCatch {
            onNPCReachDestinationHandler.onNPCReachDestination(npcid.toNPC())
        }
    }

    override fun onWeaponShot(
            npcid: Int,
            weaponid: Int,
            hittype: Int,
            hitid: Int,
            fX: Float,
            fY: Float,
            fZ: Float
    ): Boolean {
        val result = tryAndCatch {
            val target = when (BulletHitType[hittype]) {
                BulletHitType.NONE -> NoHitTarget
                BulletHitType.PLAYER -> PlayerHitTarget(hitid.toPlayer())
                BulletHitType.VEHICLE -> VehicleHitTarget(hitid.toVehicle())
                BulletHitType.OBJECT -> MapObjectHitTarget(hitid.toMapObject())
                BulletHitType.PLAYER_OBJECT ->
                    PlayerMapObjectHitTarget(hitid.toPlayerMapObject(npcid.toPlayer()))
            }
            onNPCWeaponShotHandler.onNPCWeaponShot(npcid.toNPC(), WeaponModel[weaponid], target, vector3DOf(fX, fY, fZ))
        } ?: OnNPCWeaponShotListener.Result.AllowDamage
        return result.value
    }

    override fun onWeaponStateChange(npcid: Int, weapon_state: Int) {
        tryAndCatch {
            onNPCWeaponStateChangeHandler.onNPCWeaponStateChange(npcid.toNPC(), WeaponState[weapon_state])
        }
    }

    override fun onStreamIn(npcid: Int, forplayerid: Int): Boolean {
        val result = tryAndCatch {
            onNPCStreamInHandler.onNPCStreamIn(npcid.toNPC(), forplayerid.toPlayer())
        } ?: OnNPCStreamInListener.Result.Sync
        return result.value
    }

    override fun onStreamOut(npcid: Int, forplayerid: Int): Boolean {
        val result = tryAndCatch {
            onNPCStreamOutHandler.onNPCStreamOut(npcid.toNPC(), forplayerid.toPlayer())
        } ?: OnNPCStreamOutListener.Result.Sync
        return result.value
    }

    override fun onVehicleEntryComplete(npcid: Int, vehicleid: Int, seatid: Int) {
        onVehicleEntryCompleteHandler.onVehicleEntryComplete(npcid.toNPC(), vehicleid.toVehicle(), seatid)
    }

    override fun onVehicleExitComplete(npcid: Int, vehicleid: Int) {
        onVehicleExitCompleteHandler.onVehicleExitComplete(npcid.toNPC(), vehicleid.toVehicle())
    }

    override fun onVehicleTakeDamage(
            npcid: Int,
            issuerid: Int,
            vehicleid: Int,
            amount: Float,
            weaponid: Int,
            fX: Float,
            fY: Float,
            fZ: Float
    ): Boolean {
        val result = tryAndCatch {
            onVehicleTakeDamageHandler.onVehicleTakeDamage(
                    npcid.toNPC(),
                    vehicleid.toVehicle(),
                    issuerid.toPlayerOrNull(),
                    amount,
                    WeaponModel[weaponid],
                    vector3DOf(fX, fY, fZ)
            )
        } ?: OnVehicleTakeDamageListener.Result.Sync
        return result.value
    }

    override fun onFinishPlayback(npcid: Int) {
        tryAndCatch {
            onNPCFinishPlaybackHandler.onNPCFinishPlayback(npcid.toNPC())
        }
    }

    override fun onFinishNode(npcid: Int, nodeid: Int) {
        tryAndCatch {
            onNPCFinishNodeHandler.onNPCFinishNode(npcid.toNPC(), nodeid.toNode())
        }
    }

    override fun onFinishNodePoint(npcid: Int, nodeid: Int, pointid: Int): Boolean {
        val result = tryAndCatch {
            onNPCFinishNodePointHandler.onNPCFinishNodePoint(npcid.toNPC(), nodeid.toNode(), pointid)
        } ?: OnNPCFinishNodePointListener.Result.Continue
        return result.value
    }

    override fun onChangeNode(npcid: Int, newnodeid: Int, oldnodeid: Int): Boolean {
        val result = tryAndCatch {
            onNPCChangeNodeHandler.onNPCChangeNode(
                    npc = npcid.toNPC(),
                    oldNode = oldnodeid.toNode(),
                    newNode = newnodeid.toNode()
            )
        } ?: OnNPCChangeNodeListener.Result.Continue
        return result.value
    }

    override fun onFinishMovePath(npcid: Int, pathid: Int) {
        tryAndCatch {
            onNPCFinishMovePathHandler.onNPCFinishMovePath(npcid.toNPC(), pathid.toMovePath())
        }
    }

    override fun onFinishMovePathPoint(npcid: Int, pathid: Int, pointid: Int): Boolean {
        val result = tryAndCatch {
            val movePathPoint = pathid.toMovePath()[MovePathPointId.valueOf(pointid)]
            onNPCFinishMovePathPointHandler.onNPCFinishMovePathPoint(npcid.toNPC(), movePathPoint)
        } ?: OnNPCFinishMovePathPointListener.Result.Continue
        return result.value
    }

    override fun onChangeHeightPos(npcid: Int, newz: Float, oldz: Float) {
        tryAndCatch {
            onNPCChangeHeightHandler.onNPCChangeHeight(npc = npcid.toNPC(), oldZ = oldz, newZ = newz)
        }
    }
}