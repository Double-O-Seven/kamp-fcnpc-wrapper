package ch.leadrian.samp.kamp.fcnpcwrapper.callback

import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponState
import ch.leadrian.samp.kamp.core.api.data.MapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.NoHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerMapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.VehicleHitTarget
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FCNPCEntityResolver
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.FullyControllableNPC
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePath
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.MovePathPoint
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathPointId
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class FCNPCCallbackProcessorSpec : Spek({
    val onNPCCreateHandler by memoized { mockk<OnNPCCreateHandler>() }
    val onNPCDestroyHandler by memoized { mockk<OnNPCDestroyHandler>() }
    val onNPCSpawnHandler by memoized { mockk<OnNPCSpawnHandler>() }
    val onNPCRespawnHandler by memoized { mockk<OnNPCRespawnHandler>() }
    val onNPCDeathHandler by memoized { mockk<OnNPCDeathHandler>() }
    val onNPCUpdateHandler by memoized { mockk<OnNPCUpdateHandler>() }
    val onNPCTakeDamageHandler by memoized { mockk<OnNPCTakeDamageHandler>() }
    val onNPCGiveDamageHandler by memoized { mockk<OnNPCGiveDamageHandler>() }
    val onNPCReachDestinationHandler by memoized { mockk<OnNPCReachDestinationHandler>() }
    val onNPCWeaponShotHandler by memoized { mockk<OnNPCWeaponShotHandler>() }
    val onNPCWeaponStateChangeHandler by memoized { mockk<OnNPCWeaponStateChangeHandler>() }
    val onNPCStreamInHandler by memoized { mockk<OnNPCStreamInHandler>() }
    val onNPCStreamOutHandler by memoized { mockk<OnNPCStreamOutHandler>() }
    val onVehicleEntryCompleteHandler by memoized { mockk<OnVehicleEntryCompleteHandler>() }
    val onVehicleExitCompleteHandler by memoized { mockk<OnVehicleExitCompleteHandler>() }
    val onVehicleTakeDamageHandler by memoized { mockk<OnVehicleTakeDamageHandler>() }
    val onNPCFinishPlaybackHandler by memoized { mockk<OnNPCFinishPlaybackHandler>() }
    val onNPCFinishNodeHandler by memoized { mockk<OnNPCFinishNodeHandler>() }
    val onNPCFinishNodePointHandler by memoized { mockk<OnNPCFinishNodePointHandler>() }
    val onNPCChangeNodeHandler by memoized { mockk<OnNPCChangeNodeHandler>() }
    val onNPCFinishMovePathHandler by memoized { mockk<OnNPCFinishMovePathHandler>() }
    val onNPCFinishMovePathPointHandler by memoized { mockk<OnNPCFinishMovePathPointHandler>() }
    val onNPCChangeHeightHandler by memoized { mockk<OnNPCChangeHeightHandler>() }
    val entityResolver by memoized { mockk<FCNPCEntityResolver>() }
    lateinit var fcnpcCallbackProcessor: FCNPCCallbackProcessor

    beforeEachTest {
        fcnpcCallbackProcessor = FCNPCCallbackProcessor(
                onNPCCreateHandler,
                onNPCDestroyHandler,
                onNPCSpawnHandler,
                onNPCRespawnHandler,
                onNPCDeathHandler,
                onNPCUpdateHandler,
                onNPCTakeDamageHandler,
                onNPCGiveDamageHandler,
                onNPCReachDestinationHandler,
                onNPCWeaponShotHandler,
                onNPCWeaponStateChangeHandler,
                onNPCStreamInHandler,
                onNPCStreamOutHandler,
                onVehicleEntryCompleteHandler,
                onVehicleExitCompleteHandler,
                onVehicleTakeDamageHandler,
                onNPCFinishPlaybackHandler,
                onNPCFinishNodeHandler,
                onNPCFinishNodePointHandler,
                onNPCChangeNodeHandler,
                onNPCFinishMovePathHandler,
                onNPCFinishMovePathPointHandler,
                onNPCChangeHeightHandler,
                entityResolver
        )
    }

    describe("onCreate") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { onNPCCreateHandler.onNPCCreate(any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            fcnpcCallbackProcessor.onCreate(npcId)
        }

        it("should call onNPCCreateHandler.onNPCCreate") {
            verify { onNPCCreateHandler.onNPCCreate(npc) }
        }
    }

    describe("onDestroy") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { onNPCDestroyHandler.onNPCDestroy(any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            fcnpcCallbackProcessor.onDestroy(npcId)
        }

        it("should call onNPCDestroyHandler.onNPCDestroy") {
            verify { onNPCDestroyHandler.onNPCDestroy(npc) }
        }
    }

    describe("onSpawn") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { onNPCSpawnHandler.onNPCSpawn(any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            fcnpcCallbackProcessor.onSpawn(npcId)
        }

        it("should call onNPCSpawnHandler.onNPCSpawn") {
            verify { onNPCSpawnHandler.onNPCSpawn(npc) }
        }
    }

    describe("onRespawn") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { onNPCRespawnHandler.onNPCRespawn(any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            fcnpcCallbackProcessor.onRespawn(npcId)
        }

        it("should call onNPCRespawnHandler.onNPCRespawn") {
            verify { onNPCRespawnHandler.onNPCRespawn(npc) }
        }
    }

    describe("onDeath") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        context("no killer") {
            beforeEach {
                every { onNPCDeathHandler.onNPCDeath(any(), any(), any()) } just Runs
                every { entityResolver.run { npcId.toNPC() } } returns npc
                every { entityResolver.run { SAMPConstants.INVALID_PLAYER_ID.toPlayerOrNull() } } returns null
                fcnpcCallbackProcessor.onDeath(npcId, SAMPConstants.INVALID_PLAYER_ID, WeaponModel.AK47.value)
            }

            it("should call onNPCDeathHandler.onNPCDeath") {
                verify { onNPCDeathHandler.onNPCDeath(npc, null, WeaponModel.AK47) }
            }
        }

        context("killer is a player") {
            val playerId = 1337
            val player by memoized { mockk<Player>() }

            beforeEach {
                every { onNPCDeathHandler.onNPCDeath(any(), any(), any()) } just Runs
                every { entityResolver.run { npcId.toNPC() } } returns npc
                every { entityResolver.run { playerId.toPlayerOrNull() } } returns player
                fcnpcCallbackProcessor.onDeath(npcId, playerId, WeaponModel.AK47.value)
            }

            it("should call onNPCDeathHandler.onNPCDeath") {
                verify { onNPCDeathHandler.onNPCDeath(npc, player, WeaponModel.AK47) }
            }
        }
    }

    describe("onUpdate") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
        }

        listOf(OnNPCUpdateListener.Result.Sync, OnNPCUpdateListener.Result.Desync).forEach { expectedResult ->
            context("result is $expectedResult") {
                var result: Boolean? = null

                beforeEach {
                    every { onNPCUpdateHandler.onNPCUpdate(npc) } returns expectedResult
                    result = fcnpcCallbackProcessor.onUpdate(npcId)
                }

                it("should return ${expectedResult.value}") {
                    assertThat(result)
                            .isEqualTo(expectedResult.value)
                }
            }
        }

        context("onNPCUpdateHandler.onNPCUpdate throws exception") {
            var result: Boolean? = null

            beforeEach {
                every { onNPCUpdateHandler.onNPCUpdate(npc) } throws Exception()
                result = fcnpcCallbackProcessor.onUpdate(npcId)
            }

            it("should return true") {
                assertThat(result)
                        .isTrue()
            }
        }
    }

    describe("onTakeDamage") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val playerId = 69
        val player by memoized { mockk<Player>() }

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
        }

        listOf(OnNPCTakeDamageListener.Result.Allow, OnNPCTakeDamageListener.Result.Prevent).forEach { expectedResult ->
            context("result is $expectedResult") {
                var result: Boolean? = null

                beforeEach {
                    every { entityResolver.run { playerId.toPlayerOrNull() } } returns player
                    every {
                        onNPCTakeDamageHandler.onNPCTakeDamage(
                                npc,
                                player,
                                13.37f,
                                WeaponModel.AK47,
                                BodyPart.GROIN
                        )
                    } returns expectedResult
                    result = fcnpcCallbackProcessor.onTakeDamage(
                            npcId,
                            playerId,
                            13.37f,
                            WeaponModel.AK47.value,
                            BodyPart.GROIN.value
                    )
                }

                it("should return ${expectedResult.value}") {
                    assertThat(result)
                            .isEqualTo(expectedResult.value)
                }
            }
        }

        context("onNPCTakeDamageHandler.onNPCTakeDamage throws exception") {
            var result: Boolean? = null

            beforeEach {
                every {
                    onNPCTakeDamageHandler.onNPCTakeDamage(
                            npc,
                            player,
                            13.37f,
                            WeaponModel.AK47,
                            BodyPart.GROIN
                    )
                } throws Exception()
                result = fcnpcCallbackProcessor.onTakeDamage(
                        npcId,
                        playerId,
                        13.37f,
                        WeaponModel.AK47.value,
                        BodyPart.GROIN.value
                )
            }

            it("should return true") {
                assertThat(result)
                        .isTrue()
            }
        }
    }

    describe("onGiveDamage") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val playerId = 69
        val player by memoized { mockk<Player>() }

        beforeEach {
            every { onNPCGiveDamageHandler.onNPCGiveDamage(any(), any(), any(), any(), any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { playerId.toPlayer() } } returns player
            fcnpcCallbackProcessor.onGiveDamage(npcId, playerId, 13.37f, WeaponModel.AK47.value, BodyPart.GROIN.value)
        }

        it("should call onNPCGiveDamageHandler.onNPCGiveDamage") {
            verify { onNPCGiveDamageHandler.onNPCGiveDamage(npc, player, 13.37f, WeaponModel.AK47, BodyPart.GROIN) }
        }
    }

    describe("onReachDestination") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { onNPCReachDestinationHandler.onNPCReachDestination(any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            fcnpcCallbackProcessor.onReachDestination(npcId)
        }

        it("should call onNPCReachDestinationHandler.onNPCReachDestination") {
            verify { onNPCReachDestinationHandler.onNPCReachDestination(npc) }
        }
    }

    describe("onWeaponShot") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
        }

        listOf(
                OnNPCWeaponShotListener.Result.AllowDamage,
                OnNPCWeaponShotListener.Result.PreventDamage
        ).forEach { expectedResult ->
            context("result is $expectedResult") {
                context("hit target is player") {
                    val playerId = 69
                    val player by memoized { mockk<Player>() }
                    var result: Boolean? = null

                    beforeEach {
                        every { entityResolver.run { playerId.toPlayer() } } returns player
                        every {
                            onNPCWeaponShotHandler.onNPCWeaponShot(
                                    npc,
                                    WeaponModel.AK47,
                                    PlayerHitTarget(player),
                                    vector3DOf(1f, 2f, 3f)
                            )
                        } returns expectedResult
                        result = fcnpcCallbackProcessor.onWeaponShot(
                                npcId,
                                WeaponModel.AK47.value,
                                SAMPConstants.BULLET_HIT_TYPE_PLAYER,
                                playerId,
                                1f, 2f, 3f
                        )
                    }

                    it("should call onNPCRespawnHandler.onNPCRespawn") {
                        assertThat(result)
                                .isEqualTo(expectedResult.value)
                    }
                }

                context("hit target is vehicle") {
                    val vehicleId = 69
                    val vehicle by memoized { mockk<Vehicle>() }
                    var result: Boolean? = null

                    beforeEach {
                        every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
                        every {
                            onNPCWeaponShotHandler.onNPCWeaponShot(
                                    npc,
                                    WeaponModel.AK47,
                                    VehicleHitTarget(vehicle),
                                    vector3DOf(1f, 2f, 3f)
                            )
                        } returns expectedResult
                        result = fcnpcCallbackProcessor.onWeaponShot(
                                npcId,
                                WeaponModel.AK47.value,
                                SAMPConstants.BULLET_HIT_TYPE_VEHICLE,
                                vehicleId,
                                1f, 2f, 3f
                        )
                    }

                    it("should return ${expectedResult.value}") {
                        assertThat(result)
                                .isEqualTo(expectedResult.value)
                    }
                }

                context("hit target is map object") {
                    val mapObjectId = 69
                    val mapObject by memoized { mockk<MapObject>() }
                    var result: Boolean? = null

                    beforeEach {
                        every { entityResolver.run { mapObjectId.toMapObject() } } returns mapObject
                        every {
                            onNPCWeaponShotHandler.onNPCWeaponShot(
                                    npc,
                                    WeaponModel.AK47,
                                    MapObjectHitTarget(mapObject),
                                    vector3DOf(1f, 2f, 3f)
                            )
                        } returns expectedResult
                        result = fcnpcCallbackProcessor.onWeaponShot(
                                npcId,
                                WeaponModel.AK47.value,
                                SAMPConstants.BULLET_HIT_TYPE_OBJECT,
                                mapObjectId,
                                1f, 2f, 3f
                        )
                    }

                    it("should return ${expectedResult.value}") {
                        assertThat(result)
                                .isEqualTo(expectedResult.value)
                    }
                }

                context("hit target is player map object") {
                    val player by memoized { mockk<Player>() }
                    val playerMapObjectId = 69
                    val playerMapObject by memoized { mockk<PlayerMapObject>() }
                    var result: Boolean? = null

                    beforeEach {
                        every { entityResolver.run { playerMapObjectId.toPlayerMapObject(player) } } returns playerMapObject
                        every { entityResolver.run { npcId.toPlayer() } } returns player
                        every {
                            onNPCWeaponShotHandler.onNPCWeaponShot(
                                    npc,
                                    WeaponModel.AK47,
                                    PlayerMapObjectHitTarget(playerMapObject),
                                    vector3DOf(1f, 2f, 3f)
                            )
                        } returns expectedResult
                        result = fcnpcCallbackProcessor.onWeaponShot(
                                npcId,
                                WeaponModel.AK47.value,
                                SAMPConstants.BULLET_HIT_TYPE_PLAYER_OBJECT,
                                playerMapObjectId,
                                1f, 2f, 3f
                        )
                    }

                    it("should return ${expectedResult.value}") {
                        assertThat(result)
                                .isEqualTo(expectedResult.value)
                    }
                }

                context("hit target is nothing") {
                    var result: Boolean? = null

                    beforeEach {
                        every {
                            onNPCWeaponShotHandler.onNPCWeaponShot(
                                    npc,
                                    WeaponModel.AK47,
                                    NoHitTarget,
                                    vector3DOf(1f, 2f, 3f)
                            )
                        } returns expectedResult
                        result = fcnpcCallbackProcessor.onWeaponShot(
                                npcId,
                                WeaponModel.AK47.value,
                                SAMPConstants.BULLET_HIT_TYPE_NONE,
                                SAMPConstants.INVALID_PLAYER_ID,
                                1f, 2f, 3f
                        )
                    }

                    it("should call onNPCRespawnHandler.onNPCRespawn") {
                        assertThat(result)
                                .isEqualTo(expectedResult.value)
                    }
                }
            }
        }

        context("onNPCWeaponShotHandler.onNPCWeaponShot throws exception") {
            context("hit target is player") {
                val playerId = 69
                val player by memoized { mockk<Player>() }
                var result: Boolean? = null

                beforeEach {
                    every { entityResolver.run { playerId.toPlayer() } } returns player
                    every {
                        onNPCWeaponShotHandler.onNPCWeaponShot(
                                npc,
                                WeaponModel.AK47,
                                PlayerHitTarget(player),
                                vector3DOf(1f, 2f, 3f)
                        )
                    } throws Exception()
                    result = fcnpcCallbackProcessor.onWeaponShot(
                            npcId,
                            WeaponModel.AK47.value,
                            SAMPConstants.BULLET_HIT_TYPE_PLAYER,
                            playerId,
                            1f, 2f, 3f
                    )
                }

                it("should return true") {
                    assertThat(result)
                            .isTrue()
                }
            }

            context("hit target is vehicle") {
                val vehicleId = 69
                val vehicle by memoized { mockk<Vehicle>() }
                var result: Boolean? = null

                beforeEach {
                    every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
                    every {
                        onNPCWeaponShotHandler.onNPCWeaponShot(
                                npc,
                                WeaponModel.AK47,
                                VehicleHitTarget(vehicle),
                                vector3DOf(1f, 2f, 3f)
                        )
                    } throws Exception()
                    result = fcnpcCallbackProcessor.onWeaponShot(
                            npcId,
                            WeaponModel.AK47.value,
                            SAMPConstants.BULLET_HIT_TYPE_VEHICLE,
                            vehicleId,
                            1f, 2f, 3f
                    )
                }

                it("should return true") {
                    assertThat(result)
                            .isTrue()
                }
            }

            context("hit target is map object") {
                val mapObjectId = 69
                val mapObject by memoized { mockk<MapObject>() }
                var result: Boolean? = null

                beforeEach {
                    every { entityResolver.run { mapObjectId.toMapObject() } } returns mapObject
                    every {
                        onNPCWeaponShotHandler.onNPCWeaponShot(
                                npc,
                                WeaponModel.AK47,
                                MapObjectHitTarget(mapObject),
                                vector3DOf(1f, 2f, 3f)
                        )
                    } throws Exception()
                    result = fcnpcCallbackProcessor.onWeaponShot(
                            npcId,
                            WeaponModel.AK47.value,
                            SAMPConstants.BULLET_HIT_TYPE_OBJECT,
                            mapObjectId,
                            1f, 2f, 3f
                    )
                }

                it("should return true") {
                    assertThat(result)
                            .isTrue()
                }
            }

            context("hit target is player map object") {
                val player by memoized { mockk<Player>() }
                val playerMapObjectId = 69
                val playerMapObject by memoized { mockk<PlayerMapObject>() }
                var result: Boolean? = null

                beforeEach {
                    every { entityResolver.run { playerMapObjectId.toPlayerMapObject(player) } } returns playerMapObject
                    every { entityResolver.run { npcId.toPlayer() } } returns player
                    every {
                        onNPCWeaponShotHandler.onNPCWeaponShot(
                                npc,
                                WeaponModel.AK47,
                                PlayerMapObjectHitTarget(playerMapObject),
                                vector3DOf(1f, 2f, 3f)
                        )
                    } throws Exception()
                    result = fcnpcCallbackProcessor.onWeaponShot(
                            npcId,
                            WeaponModel.AK47.value,
                            SAMPConstants.BULLET_HIT_TYPE_PLAYER_OBJECT,
                            playerMapObjectId,
                            1f, 2f, 3f
                    )
                }

                it("should return true") {
                    assertThat(result)
                            .isTrue()
                }
            }

            context("hit target is nothing") {
                var result: Boolean? = null

                beforeEach {
                    every {
                        onNPCWeaponShotHandler.onNPCWeaponShot(
                                npc,
                                WeaponModel.AK47,
                                NoHitTarget,
                                vector3DOf(1f, 2f, 3f)
                        )
                    } throws Exception()
                    result = fcnpcCallbackProcessor.onWeaponShot(
                            npcId,
                            WeaponModel.AK47.value,
                            SAMPConstants.BULLET_HIT_TYPE_NONE,
                            SAMPConstants.INVALID_PLAYER_ID,
                            1f, 2f, 3f
                    )
                }

                it("should return true") {
                    assertThat(result)
                            .isTrue()
                }
            }
        }
    }

    describe("onWeaponStateChange") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { onNPCWeaponStateChangeHandler.onNPCWeaponStateChange(any(), any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            fcnpcCallbackProcessor.onWeaponStateChange(npcId, WeaponState.LAST_BULLET.value)
        }

        it("should call onNPCWeaponStateChangeHandler.onNPCWeaponStateChange") {
            verify { onNPCWeaponStateChangeHandler.onNPCWeaponStateChange(npc, WeaponState.LAST_BULLET) }
        }
    }

    describe("onStreamIn") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val playerId = 69
        val player by memoized { mockk<Player>() }

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        listOf(OnNPCStreamInListener.Result.Sync, OnNPCStreamInListener.Result.Desync).forEach { expectedResult ->
            context("result is $expectedResult") {
                var result: Boolean? = null

                beforeEach {
                    every { onNPCStreamInHandler.onNPCStreamIn(npc, player) } returns expectedResult
                    result = fcnpcCallbackProcessor.onStreamIn(npcId, playerId)
                }

                it("should return ${expectedResult.value}") {
                    assertThat(result)
                            .isEqualTo(expectedResult.value)
                }
            }
        }

        context("onNPCStreamInHandler.onNPCStreamIn throws exception") {
            var result: Boolean? = null

            beforeEach {
                every { onNPCStreamInHandler.onNPCStreamIn(npc, player) } throws Exception()
                result = fcnpcCallbackProcessor.onStreamIn(npcId, playerId)
            }

            it("should return true") {
                assertThat(result)
                        .isTrue()
            }
        }
    }

    describe("onStreamOut") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val playerId = 69
        val player by memoized { mockk<Player>() }

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        listOf(OnNPCStreamOutListener.Result.Sync, OnNPCStreamOutListener.Result.Desync).forEach { expectedResult ->
            context("result is $expectedResult") {
                var result: Boolean? = null

                beforeEach {
                    every { onNPCStreamOutHandler.onNPCStreamOut(npc, player) } returns expectedResult
                    result = fcnpcCallbackProcessor.onStreamOut(npcId, playerId)
                }

                it("should return ${expectedResult.value}") {
                    assertThat(result)
                            .isEqualTo(expectedResult.value)
                }
            }
        }

        context("onNPCStreamOutHandler.onNPCStreamOut throws exception") {
            var result: Boolean? = null

            beforeEach {
                every { onNPCStreamOutHandler.onNPCStreamOut(npc, player) } throws Exception()
                result = fcnpcCallbackProcessor.onStreamOut(npcId, playerId)
            }

            it("should return true") {
                assertThat(result)
                        .isTrue()
            }
        }
    }

    describe("onVehicleEntryComplete") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val vehicleId = 69
        val vehicle by memoized { mockk<Vehicle>() }

        beforeEach {
            every { onVehicleEntryCompleteHandler.onVehicleEntryComplete(any(), any(), any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
            fcnpcCallbackProcessor.onVehicleEntryComplete(npcId, vehicleId, 2)
        }

        it("should call onVehicleEntryCompleteHandler.onVehicleEntryComplete") {
            verify { onVehicleEntryCompleteHandler.onVehicleEntryComplete(npc, vehicle, 2) }
        }
    }

    describe("onVehicleExitComplete") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val vehicleId = 69
        val vehicle by memoized { mockk<Vehicle>() }

        beforeEach {
            every { onVehicleExitCompleteHandler.onVehicleExitComplete(any(), any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
            fcnpcCallbackProcessor.onVehicleExitComplete(npcId, vehicleId)
        }

        it("should call onVehicleExitCompleteHandler.onVehicleExitComplete") {
            verify { onVehicleExitCompleteHandler.onVehicleExitComplete(npc, vehicle) }
        }
    }

    describe("onVehicleTakeDamage") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val playerId = 69
        val player by memoized { mockk<Player>() }
        val vehicleId = 1234
        val vehicle by memoized { mockk<Vehicle>() }

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { playerId.toPlayerOrNull() } } returns player
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        }

        listOf(
                OnVehicleTakeDamageListener.Result.Sync,
                OnVehicleTakeDamageListener.Result.Desync
        ).forEach { expectedResult ->
            context("result is $expectedResult") {
                var result: Boolean? = null

                beforeEach {
                    every {
                        onVehicleTakeDamageHandler.onVehicleTakeDamage(
                                npc,
                                vehicle,
                                player,
                                13.37f,
                                WeaponModel.AK47,
                                vector3DOf(1f, 2f, 3f)
                        )
                    } returns expectedResult
                    result = fcnpcCallbackProcessor.onVehicleTakeDamage(
                            npcId,
                            playerId,
                            vehicleId,
                            13.37f,
                            WeaponModel.AK47.value,
                            1f,
                            2f,
                            3f
                    )
                }

                it("should return ${expectedResult.value}") {
                    assertThat(result)
                            .isEqualTo(expectedResult.value)
                }
            }
        }

        context("onVehicleTakeDamageHandler.onVehicleTakeDamage throws exception") {
            var result: Boolean? = null

            beforeEach {
                every {
                    onVehicleTakeDamageHandler.onVehicleTakeDamage(
                            npc,
                            vehicle,
                            player,
                            13.37f,
                            WeaponModel.AK47,
                            vector3DOf(1f, 2f, 3f)
                    )
                } throws Exception()
                result = fcnpcCallbackProcessor.onVehicleTakeDamage(
                        npcId,
                        playerId,
                        vehicleId,
                        13.37f,
                        WeaponModel.AK47.value,
                        1f,
                        2f,
                        3f
                )
            }

            it("should return true") {
                assertThat(result)
                        .isTrue()
            }
        }
    }

    describe("onFinishPlayback") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { onNPCFinishPlaybackHandler.onNPCFinishPlayback(any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            fcnpcCallbackProcessor.onFinishPlayback(npcId)
        }

        it("should call onNPCFinishPlaybackHandler.onNPCFinishPlayback") {
            verify { onNPCFinishPlaybackHandler.onNPCFinishPlayback(npc) }
        }
    }

    describe("onFinishNode") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val nodeId = 69
        val node by memoized { mockk<Node>() }

        beforeEach {
            every { onNPCFinishNodeHandler.onNPCFinishNode(any(), any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { nodeId.toNode() } } returns node
            fcnpcCallbackProcessor.onFinishNode(npcId, nodeId)
        }

        it("should call onNPCFinishNodeHandler.onNPCChangeNode") {
            verify { onNPCFinishNodeHandler.onNPCFinishNode(npc, node) }
        }
    }

    describe("onNPCFinishNodePoint") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val nodeId = 1234
        val node by memoized { mockk<Node>() }
        val nodePointId = 69

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { nodeId.toNode() } } returns node
        }

        listOf(
                OnNPCFinishNodePointListener.Result.Continue,
                OnNPCFinishNodePointListener.Result.Stop
        ).forEach { expectedResult ->
            context("result is $expectedResult") {
                var result: Boolean? = null

                beforeEach {
                    every {
                        onNPCFinishNodePointHandler.onNPCFinishNodePoint(
                                npc,
                                node,
                                nodePointId
                        )
                    } returns expectedResult
                    result = fcnpcCallbackProcessor.onFinishNodePoint(npcId, nodeId, nodePointId)
                }

                it("should return ${expectedResult.value}") {
                    assertThat(result)
                            .isEqualTo(expectedResult.value)
                }
            }
        }

        context("onFinishNodePointHandler.onFinishNodePoint throws exception") {
            var result: Boolean? = null

            beforeEach {
                every { onNPCFinishNodePointHandler.onNPCFinishNodePoint(npc, node, nodePointId) } throws Exception()
                result = fcnpcCallbackProcessor.onFinishNodePoint(npcId, nodeId, nodePointId)
            }

            it("should return true") {
                assertThat(result)
                        .isTrue()
            }
        }
    }

    describe("onNPCChangeNode") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val oldNodeId = 1234
        val oldNode by memoized { mockk<Node>() }
        val newNodeId = 5678
        val newNode by memoized { mockk<Node>() }

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { oldNodeId.toNode() } } returns oldNode
            every { entityResolver.run { newNodeId.toNode() } } returns newNode
        }

        listOf(
                OnNPCChangeNodeListener.Result.Continue,
                OnNPCChangeNodeListener.Result.Stop
        ).forEach { expectedResult ->
            context("result is $expectedResult") {
                var result: Boolean? = null

                beforeEach {
                    every {
                        onNPCChangeNodeHandler.onNPCChangeNode(
                                npc = npc,
                                oldNode = oldNode,
                                newNode = newNode
                        )
                    } returns expectedResult
                    result = fcnpcCallbackProcessor.onChangeNode(
                            npcid = npcId,
                            oldnodeid = oldNodeId,
                            newnodeid = newNodeId
                    )
                }

                it("should return ${expectedResult.value}") {
                    assertThat(result)
                            .isEqualTo(expectedResult.value)
                }
            }
        }

        context("onNPCChangeNodeHandler.onNPCChangeNode throws exception") {
            var result: Boolean? = null

            beforeEach {
                every {
                    onNPCChangeNodeHandler.onNPCChangeNode(
                            npc = npc,
                            oldNode = oldNode,
                            newNode = newNode
                    )
                } throws Exception()
                result = fcnpcCallbackProcessor.onChangeNode(
                        npcid = npcId,
                        oldnodeid = oldNodeId,
                        newnodeid = newNodeId
                )
            }

            it("should return true") {
                assertThat(result)
                        .isTrue()
            }
        }
    }

    describe("onFinishMovePath") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val movePathId = 69
        val movePath by memoized { mockk<MovePath>() }

        beforeEach {
            every { onNPCFinishMovePathHandler.onNPCFinishMovePath(any(), any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { movePathId.toMovePath() } } returns movePath
            fcnpcCallbackProcessor.onFinishMovePath(npcId, movePathId)
        }

        it("should call onNPCFinishMovePathHandler.onNPCChangeMovePath") {
            verify { onNPCFinishMovePathHandler.onNPCFinishMovePath(npc, movePath) }
        }
    }

    describe("onNPCFinishMovePathPoint") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val movePathPointId = 69
        val movePathPoint by memoized { mockk<MovePathPoint>() }
        val movePathId = 1234
        val movePath by memoized {
            mockk<MovePath> {
                every { get(MovePathPointId.valueOf(movePathPointId)) } returns movePathPoint
            }
        }

        beforeEach {
            every { entityResolver.run { npcId.toNPC() } } returns npc
            every { entityResolver.run { movePathId.toMovePath() } } returns movePath
        }

        listOf(
                OnNPCFinishMovePathPointListener.Result.Continue,
                OnNPCFinishMovePathPointListener.Result.Stop
        ).forEach { expectedResult ->
            context("result is $expectedResult") {
                var result: Boolean? = null

                beforeEach {
                    every {
                        onNPCFinishMovePathPointHandler.onNPCFinishMovePathPoint(npc, movePathPoint)
                    } returns expectedResult
                    result = fcnpcCallbackProcessor.onFinishMovePathPoint(npcId, movePathId, movePathPointId)
                }

                it("should return ${expectedResult.value}") {
                    assertThat(result)
                            .isEqualTo(expectedResult.value)
                }
            }
        }

        context("onFinishMovePathPointHandler.onFinishMovePathPoint throws exception") {
            var result: Boolean? = null

            beforeEach {
                every {
                    onNPCFinishMovePathPointHandler.onNPCFinishMovePathPoint(npc, movePathPoint)
                } throws Exception()
                result = fcnpcCallbackProcessor.onFinishMovePathPoint(npcId, movePathId, movePathPointId)
            }

            it("should return true") {
                assertThat(result)
                        .isTrue()
            }
        }
    }

    describe("onChangeHeightPos") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }

        beforeEach {
            every { onNPCChangeHeightHandler.onNPCChangeHeight(any(), any(), any()) } just Runs
            every { entityResolver.run { npcId.toNPC() } } returns npc
            fcnpcCallbackProcessor.onChangeHeightPos(npcid = npcId, newz = 1f, oldz = 2f)
        }

        it("should call onNPCChangeHeightHandler.onNPCChangeHeight") {
            verify { onNPCChangeHeightHandler.onNPCChangeHeight(npc = npc, newZ = 1f, oldZ = 2f) }
        }
    }
})