package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.constants.FightingStyle
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.HitTarget
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.EntityCheck
import ch.leadrian.samp.kamp.fcnpcwrapper.data.AimParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.data.NearbyTarget
import ch.leadrian.samp.kamp.fcnpcwrapper.data.WeaponShotParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.EnumSet

object FCNPCCombatSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val playerService by memoized { mockk<PlayerService>() }
    val hitTargetResolver by memoized { mockk<HitTargetResolver>() }
    val npcId = 123
    val npc by memoized {
        mockk<FullyControllableNPC> {
            every { id } returns FullyControllableNPCId.valueOf(npcId)
        }
    }
    val combat by memoized {
        FCNPCCombat(npc, fcnpcNativeFunctions, playerService, hitTargetResolver)
    }

    describe("fightingStyle") {
        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.getFightingStyle(npcId) } returns FightingStyle.KUNGFU.value
            }

            it("should return fighting style") {
                assertThat(combat.fightingStyle)
                        .isEqualTo(FightingStyle.KUNGFU)
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setFightingStyle(any(), any()) } returns true
                combat.fightingStyle = FightingStyle.BOXING
            }

            it("should call fcnpcNativeFunctions.setFightingStyle") {
                verify { fcnpcNativeFunctions.setFightingStyle(npcid = npcId, style = FightingStyle.BOXING.value) }
            }
        }
    }

    describe("isReloadingUsed") {
        describe("getter") {
            listOf(true, false).forEach { isReloadingUsed ->
                context("fcnpcNativeFunctions.isReloadingUsed returns $isReloadingUsed") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isReloadingUsed(npcId) } returns isReloadingUsed
                    }

                    it("should return $isReloadingUsed") {
                        assertThat(combat.isReloadingUsed)
                                .isEqualTo(isReloadingUsed)
                    }
                }
            }
        }

        describe("setter") {
            listOf(true, false).forEach { isReloadingUsed ->
                context("value is set to $isReloadingUsed") {
                    beforeEach {
                        every { fcnpcNativeFunctions.useReloading(any(), any()) } returns true
                        combat.isReloadingUsed = isReloadingUsed
                    }

                    it("should call fcnpcNativeFunctions.useReloading") {
                        verify { fcnpcNativeFunctions.useReloading(npcId, isReloadingUsed) }
                    }
                }
            }
        }
    }

    describe("isInfiniteAmmoUsed") {
        describe("getter") {
            listOf(true, false).forEach { isInfiniteAmmoUsed ->
                context("fcnpcNativeFunctions.isInfiniteAmmoUsed returns $isInfiniteAmmoUsed") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isInfiniteAmmoUsed(npcId) } returns isInfiniteAmmoUsed
                    }

                    it("should return $isInfiniteAmmoUsed") {
                        assertThat(combat.isInfiniteAmmoUsed)
                                .isEqualTo(isInfiniteAmmoUsed)
                    }
                }
            }
        }

        describe("setter") {
            listOf(true, false).forEach { isInfiniteAmmoUsed ->
                context("value is set to $isInfiniteAmmoUsed") {
                    beforeEach {
                        every { fcnpcNativeFunctions.useInfiniteAmmo(any(), any()) } returns true
                        combat.isInfiniteAmmoUsed = isInfiniteAmmoUsed
                    }

                    it("should call fcnpcNativeFunctions.useInfiniteAmmo") {
                        verify { fcnpcNativeFunctions.useInfiniteAmmo(npcId, isInfiniteAmmoUsed) }
                    }
                }
            }
        }
    }

    describe("isAttacking") {
        listOf(true, false).forEach { isAttacking ->
            context("fcnpcNativeFunctions.isAttacking returns $isAttacking") {
                beforeEach {
                    every { fcnpcNativeFunctions.isAttacking(npcId) } returns isAttacking
                }

                it("should return $isAttacking") {
                    assertThat(combat.isAttacking)
                            .isEqualTo(isAttacking)
                }
            }
        }
    }

    describe("isAiming") {
        listOf(true, false).forEach { isAiming ->
            context("fcnpcNativeFunctions.isAiming returns $isAiming") {
                beforeEach {
                    every { fcnpcNativeFunctions.isAiming(npcId) } returns isAiming
                }

                it("should return $isAiming") {
                    assertThat(combat.isAiming)
                            .isEqualTo(isAiming)
                }
            }
        }
    }

    describe("aimingPlayer") {
        context("player is not connected") {
            beforeEach {
                every { fcnpcNativeFunctions.getAimingPlayer(npcId) } returns SAMPConstants.INVALID_PLAYER_ID
                every { playerService.isPlayerConnected(PlayerId.valueOf(SAMPConstants.INVALID_PLAYER_ID)) } returns false
            }

            it("should return null") {
                assertThat(combat.aimingPlayer)
                        .isNull()
            }
        }

        context("player ID is connected") {
            val player by memoized { mockk<Player>() }
            beforeEach {
                val playerId = 69
                every { fcnpcNativeFunctions.getAimingPlayer(npcId) } returns playerId
                every { playerService.isPlayerConnected(PlayerId.valueOf(playerId)) } returns true
                every { playerService.getPlayer(PlayerId.valueOf(playerId)) } returns player
            }

            it("should return player") {
                assertThat(combat.aimingPlayer)
                        .isEqualTo(player)
            }
        }
    }

    describe("isShooting") {
        listOf(true, false).forEach { isShooting ->
            context("fcnpcNativeFunctions.isShooting returns $isShooting") {
                beforeEach {
                    every { fcnpcNativeFunctions.isShooting(npcId) } returns isShooting
                }

                it("should return $isShooting") {
                    assertThat(combat.isShooting)
                            .isEqualTo(isShooting)
                }
            }
        }
    }

    describe("isReloading") {
        listOf(true, false).forEach { isReloading ->
            context("fcnpcNativeFunctions.isReloading returns $isReloading") {
                beforeEach {
                    every { fcnpcNativeFunctions.isReloading(npcId) } returns isReloading
                }

                it("should return $isReloading") {
                    assertThat(combat.isReloading)
                            .isEqualTo(isReloading)
                }
            }
        }
    }

    describe("aimAt") {
        context("coordinates as target") {
            beforeEach {
                every {
                    fcnpcNativeFunctions.aimAt(
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any()
                    )
                } returns true
            }

            context("default parameter values") {
                beforeEach {
                    combat.aimAt(vector3DOf(1f, 2f, 3f), AimParameters())
                }

                it("should call fcnpcNativeFunctions.aimAt") {
                    verify {
                        fcnpcNativeFunctions.aimAt(
                                npcid = npcId,
                                x = 1f,
                                y = 2f,
                                z = 3f,
                                shoot = false,
                                shoot_delay = -1,
                                set_angle = true,
                                offset_from_x = 0f,
                                offset_from_y = 0f,
                                offset_from_z = 0f,
                                between_check_flags = EntityCheck.ALL.value
                        )
                    }
                }
            }

            listOf(true, false).forEach { shoot ->
                listOf(true, false).forEach { setAngle ->
                    context("all parameters set, shoot set to $shoot, shoot set to $setAngle") {
                        beforeEach {
                            combat.aimAt(
                                    vector3DOf(1f, 2f, 3f),
                                    AimParameters(
                                            shoot = shoot,
                                            shootDelay = 13,
                                            offsetFrom = vector3DOf(4f, 5f, 6f),
                                            setAngle = setAngle,
                                            betweenChecks = EnumSet.of(EntityCheck.PLAYER, EntityCheck.NPC)
                                    )
                            )
                        }

                        it("should call fcnpcNativeFunctions.aimAt") {
                            verify {
                                fcnpcNativeFunctions.aimAt(
                                        npcid = npcId,
                                        x = 1f,
                                        y = 2f,
                                        z = 3f,
                                        shoot = shoot,
                                        shoot_delay = 13,
                                        set_angle = setAngle,
                                        offset_from_x = 4f,
                                        offset_from_y = 5f,
                                        offset_from_z = 6f,
                                        between_check_flags = EntityCheck.PLAYER.value or EntityCheck.NPC.value
                                )
                            }
                        }
                    }
                }
            }
        }

        context("player as target") {
            val playerId = 69
            val player by memoized {
                mockk<Player> {
                    every { id } returns PlayerId.valueOf(playerId)
                }
            }
            beforeEach {
                every {
                    fcnpcNativeFunctions.aimAtPlayer(
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any(),
                            any()
                    )
                } returns true
            }

            context("default parameters") {
                beforeEach {
                    combat.aimAt(player)
                }

                it("should call fcnpcNativeFunctions.aimAt") {
                    verify {
                        fcnpcNativeFunctions.aimAtPlayer(
                                npcid = npcId,
                                playerid = playerId,
                                shoot = false,
                                shoot_delay = -1,
                                set_angle = true,
                                offset_x = 0f,
                                offset_y = 0f,
                                offset_z = 0f,
                                offset_from_x = 0f,
                                offset_from_y = 0f,
                                offset_from_z = 0f,
                                between_check_flags = EntityCheck.ALL.value
                        )
                    }
                }
            }

            context("default aim parameter values") {
                beforeEach {
                    combat.aimAt(player, AimParameters(), vector3DOf(1f, 2f, 3f))
                }

                it("should call fcnpcNativeFunctions.aimAt") {
                    verify {
                        fcnpcNativeFunctions.aimAtPlayer(
                                npcid = npcId,
                                playerid = playerId,
                                shoot = false,
                                shoot_delay = -1,
                                set_angle = true,
                                offset_x = 1f,
                                offset_y = 2f,
                                offset_z = 3f,
                                offset_from_x = 0f,
                                offset_from_y = 0f,
                                offset_from_z = 0f,
                                between_check_flags = EntityCheck.ALL.value
                        )
                    }
                }
            }

            listOf(true, false).forEach { shoot ->
                listOf(true, false).forEach { setAngle ->
                    context("all aim parameters values set, shoot set to $shoot, shoot set to $setAngle") {
                        beforeEach {
                            combat.aimAt(
                                    player,
                                    AimParameters(
                                            shoot = shoot,
                                            shootDelay = 13,
                                            offsetFrom = vector3DOf(4f, 5f, 6f),
                                            setAngle = setAngle,
                                            betweenChecks = EnumSet.of(EntityCheck.PLAYER, EntityCheck.NPC)
                                    ),
                                    vector3DOf(1f, 2f, 3f)
                            )
                        }

                        it("should call fcnpcNativeFunctions.aimAt") {
                            verify {
                                fcnpcNativeFunctions.aimAtPlayer(
                                        npcid = npcId,
                                        playerid = playerId,
                                        shoot = shoot,
                                        shoot_delay = 13,
                                        set_angle = setAngle,
                                        offset_x = 1f,
                                        offset_y = 2f,
                                        offset_z = 3f,
                                        offset_from_x = 4f,
                                        offset_from_y = 5f,
                                        offset_from_z = 6f,
                                        between_check_flags = EntityCheck.PLAYER.value or EntityCheck.NPC.value
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    describe("stopAim") {
        beforeEach {
            every { fcnpcNativeFunctions.stopAim(any()) } returns true
            combat.stopAim()
        }

        it("should call fcnpcNativeFunctions.stopAim") {
            verify { fcnpcNativeFunctions.stopAim(npcId) }
        }
    }

    describe("meleeAttack") {
        beforeEach {
            every { fcnpcNativeFunctions.meleeAttack(any(), any(), any()) } returns true
        }

        context("with default parameters") {
            beforeEach {
                combat.meleeAttack()
            }

            it("should call fcnpcNativeFunctions.meleeAttack") {
                verify { fcnpcNativeFunctions.meleeAttack(npcid = npcId, delay = -1, fightstyle = false) }
            }
        }

        listOf(true, false).forEach { useFightingStyle ->
            context("with useFightingStyle set to $useFightingStyle") {
                beforeEach {
                    combat.meleeAttack(69, useFightingStyle)
                }

                it("should call fcnpcNativeFunctions.meleeAttack") {
                    verify {
                        fcnpcNativeFunctions.meleeAttack(npcid = npcId, delay = 69, fightstyle = useFightingStyle)
                    }
                }
            }
        }
    }

    describe("stopAttack") {
        beforeEach {
            every { fcnpcNativeFunctions.stopAttack(any()) } returns true
            combat.stopAttack()
        }

        it("should call fcnpcNativeFunctions.stopAttack") {
            verify { fcnpcNativeFunctions.stopAttack(npcId) }
        }
    }

    describe("isAimingAt") {
        val playerId = 69
        val player by memoized {
            mockk<Player> {
                every { id } returns PlayerId.valueOf(playerId)
            }
        }
        listOf(true, false).forEach { isAiming ->
            context("fcnpcNativeFunctions.isAimingAtPlayer returns $isAiming") {
                beforeEach {
                    every { fcnpcNativeFunctions.isAimingAtPlayer(npcId, playerId) } returns isAiming
                }

                it("should return $isAiming") {
                    assertThat(combat.isAimingAt(player))
                            .isEqualTo(isAiming)
                }
            }
        }
    }

    describe("triggerWeaponShot") {
        lateinit var hitTarget: HitTarget
        val hitId = 69

        beforeEach {
            every {
                fcnpcNativeFunctions.triggerWeaponShot(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns true
            hitTarget = mockk {
                every { type } returns BulletHitType.PLAYER
            }
            every { hitTargetResolver.getHitId(hitTarget) } returns hitId
        }

        context("with default parameters") {
            beforeEach {
                combat.triggerWeaponShot(
                        WeaponShotParameters(
                                weapon = WeaponModel.M4,
                                hitTarget = hitTarget,
                                coordinates = vector3DOf(1f, 2f, 3f)
                        )
                )
            }

            it("should call fcnpcNativeFunctions.triggerWeaponShot") {
                verify {
                    fcnpcNativeFunctions.triggerWeaponShot(
                            npcid = npcId,
                            weaponid = WeaponModel.M4.value,
                            hitid = hitId,
                            hittype = BulletHitType.PLAYER.value,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            is_hit = true,
                            offset_from_x = 0f,
                            offset_from_y = 0f,
                            offset_from_z = 0f,
                            between_check_flags = EntityCheck.ALL.value
                    )
                }
            }
        }

        listOf(true, false).forEach { isHit ->
            context("with parameters set, isHit set to $isHit") {
                beforeEach {
                    combat.triggerWeaponShot(
                            WeaponShotParameters(
                                    weapon = WeaponModel.M4,
                                    hitTarget = hitTarget,
                                    coordinates = vector3DOf(1f, 2f, 3f),
                                    isHit = isHit,
                                    offsetFrom = vector3DOf(4f, 5f, 6f),
                                    betweenChecks = EnumSet.of(EntityCheck.PLAYER)
                            )
                    )
                }

                it("should call fcnpcNativeFunctions.triggerWeaponShot") {
                    verify {
                        fcnpcNativeFunctions.triggerWeaponShot(
                                npcid = npcId,
                                weaponid = WeaponModel.M4.value,
                                hitid = hitId,
                                hittype = BulletHitType.PLAYER.value,
                                x = 1f,
                                y = 2f,
                                z = 3f,
                                is_hit = isHit,
                                offset_from_x = 4f,
                                offset_from_y = 5f,
                                offset_from_z = 6f,
                                between_check_flags = EntityCheck.PLAYER.value
                        )
                    }
                }
            }
        }
    }

    describe("getClosestEntityInBetween") {
        val hitTarget by memoized { mockk<HitTarget>() }
        lateinit var nearbyTarget: NearbyTarget

        beforeEach {
            val hitId = 69
            val playerMapObjectOwner = 187
            every {
                fcnpcNativeFunctions.getClosestEntityInBetween(
                        npcid = npcId,
                        x = 1f,
                        y = 2f,
                        z = 3f,
                        range = 4f,
                        between_check_flags = EntityCheck.PLAYER.value or EntityCheck.VEHICLE.value,
                        entity_id = any(),
                        entity_type = any(),
                        object_owner_id = any(),
                        point_x = any(),
                        point_y = any(),
                        point_z = any()
                )
            } answers {
                arg<MutableIntCell>(6).value = hitId
                arg<MutableIntCell>(7).value = BulletHitType.PLAYER.value
                arg<MutableIntCell>(8).value = playerMapObjectOwner
                arg<MutableFloatCell>(9).value = 5f
                arg<MutableFloatCell>(10).value = 6f
                arg<MutableFloatCell>(11).value = 7f
                true
            }
            every {
                hitTargetResolver.getHitTarget(hitId, BulletHitType.PLAYER, PlayerId.valueOf(playerMapObjectOwner))
            } returns hitTarget
            nearbyTarget = combat.getClosestEntityInBetween(
                    vector3DOf(1f, 2f, 3f),
                    4f,
                    EntityCheck.PLAYER,
                    EntityCheck.VEHICLE
            )
        }

        it("should return NearbyTarget") {
            assertThat(nearbyTarget)
                    .isEqualTo(NearbyTarget(hitTarget, vector3DOf(5f, 6f, 7f)))
        }
    }
})