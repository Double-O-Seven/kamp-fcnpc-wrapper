package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponSkill
import ch.leadrian.samp.kamp.core.api.constants.WeaponState
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.data.WeaponInfo
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class WeaponsSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val npcId = 123
    val npc by memoized {
        mockk<FullyControllableNPC> {
            every { id } returns FullyControllableNPCId.valueOf(npcId)
        }
    }
    val weapons by memoized { Weapons(npc, fcnpcNativeFunctions) }

    describe("current") {
        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.getWeapon(npcId) } returns WeaponModel.MINIGUN.value
            }

            it("should return current weapon") {
                assertThat(weapons.current)
                        .isEqualTo(WeaponModel.MINIGUN)
            }
        }

        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setWeapon(any(), any()) } returns true
                weapons.current = WeaponModel.MINIGUN
            }

            it("should call fcnpcNativeFunctions.setWeapon") {
                verify { fcnpcNativeFunctions.setWeapon(npcid = npcId, weaponid = WeaponModel.MINIGUN.value) }
            }
        }
    }

    describe("ammo") {
        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.getAmmo(npcId) } returns 69
            }

            it("should return ammo") {
                assertThat(weapons.ammo)
                        .isEqualTo(69)
            }
        }

        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.setAmmo(any(), any()) } returns true
                weapons.ammo = 1337
            }

            it("should call fcnpcNativeFunctions.setAmmo") {
                verify { fcnpcNativeFunctions.setAmmo(npcid = npcId, ammo = 1337) }
            }
        }
    }

    describe("ammoInClip") {
        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.getAmmoInClip(npcId) } returns 69
            }

            it("should return ammo in clip") {
                assertThat(weapons.ammoInClip)
                        .isEqualTo(69)
            }
        }

        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.setAmmoInClip(any(), any()) } returns true
                weapons.ammoInClip = 1337
            }

            it("should call fcnpcNativeFunctions.setAmmoInClip") {
                verify { fcnpcNativeFunctions.setAmmoInClip(npcid = npcId, ammo = 1337) }
            }
        }
    }

    describe("state") {
        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setWeaponState(any(), any()) } returns true
                weapons.state = WeaponState.RELOADING
            }

            it("should call fcnpcNativeFunctions.setWeaponState") {
                verify { fcnpcNativeFunctions.setWeaponState(npcid = npcId, weaponstate = WeaponState.RELOADING.value) }
            }
        }

        describe("getter") {
            beforeEach {
                every { fcnpcNativeFunctions.getWeaponState(npcId) } returns WeaponState.LAST_BULLET.value
            }

            it("should return weapon state") {
                assertThat(weapons.state)
                        .isEqualTo(WeaponState.LAST_BULLET)
            }
        }
    }

    describe("giveAmmo") {
        beforeEach {
            every { fcnpcNativeFunctions.giveAmmo(any(), any()) } returns true
            weapons.giveAmmo(69)
        }

        it("should call fcnpcNativeFunctions.giveAmmo") {
            verify { fcnpcNativeFunctions.giveAmmo(npcId, 69) }
        }
    }

    describe("giveAmmoInClip") {
        beforeEach {
            every { fcnpcNativeFunctions.giveAmmoInClip(any(), any()) } returns true
            weapons.giveAmmoInClip(69)
        }

        it("should call fcnpcNativeFunctions.giveAmmoInClip") {
            verify { fcnpcNativeFunctions.giveAmmoInClip(npcId, 69) }
        }
    }

    describe("setSkillLevel") {
        beforeEach {
            every { fcnpcNativeFunctions.setWeaponSkillLevel(any(), any(), any()) } returns true
            weapons.setSkillLevel(WeaponSkill.DESERT_EAGLE, 256)
        }

        it("should call fcnpcNativeFunctions.setWeaponSkillLevel") {
            verify {
                fcnpcNativeFunctions.setWeaponSkillLevel(
                        npcid = npcId,
                        skill = WeaponSkill.DESERT_EAGLE.value,
                        level = 256
                )
            }
        }
    }

    describe("giveSkillLevel") {
        beforeEach {
            every { fcnpcNativeFunctions.giveWeaponSkillLevel(any(), any(), any()) } returns true
            weapons.giveSkillLevel(WeaponSkill.DESERT_EAGLE, 256)
        }

        it("should call fcnpcNativeFunctions.giveWeaponSkillLevel") {
            verify {
                fcnpcNativeFunctions.giveWeaponSkillLevel(
                        npcid = npcId,
                        skill = WeaponSkill.DESERT_EAGLE.value,
                        level = 256
                )
            }
        }
    }

    describe("getSkillLevel") {
        beforeEach {
            every { fcnpcNativeFunctions.getWeaponSkillLevel(npcid = npcId, skill = WeaponSkill.M4.value) } returns 69
        }

        it("should return skill level") {
            assertThat(weapons.getSkillLevel(WeaponSkill.M4))
                    .isEqualTo(69)
        }
    }

    describe("setReloadTime") {
        beforeEach {
            every { fcnpcNativeFunctions.setWeaponReloadTime(any(), any(), any()) } returns true
            weapons.setReloadTime(WeaponModel.M4, 999)
        }

        it("should call fcnpcNativeFunctions.setWeaponReloadTime") {
            verify { fcnpcNativeFunctions.setWeaponReloadTime(npcId, WeaponModel.M4.value, 999) }
        }
    }

    describe("getReloadTime") {
        beforeEach {
            every { fcnpcNativeFunctions.getWeaponReloadTime(npcId, WeaponModel.M4.value) } returns 999
        }

        it("should return reload time") {
            assertThat(weapons.getReloadTime(WeaponModel.M4))
                    .isEqualTo(999)
        }
    }

    describe("getActualReloadTime") {
        beforeEach {
            every { fcnpcNativeFunctions.getWeaponActualReloadTime(npcId, WeaponModel.M4.value) } returns 999
        }

        it("should return actual reload time") {
            assertThat(weapons.getActualReloadTime(WeaponModel.M4))
                    .isEqualTo(999)
        }
    }

    describe("setShootTime") {
        beforeEach {
            every { fcnpcNativeFunctions.setWeaponShootTime(any(), any(), any()) } returns true
            weapons.setShootTime(WeaponModel.M4, 999)
        }

        it("should call fcnpcNativeFunctions.setWeaponShootTime") {
            verify { fcnpcNativeFunctions.setWeaponShootTime(npcId, WeaponModel.M4.value, 999) }
        }
    }

    describe("getShootTime") {
        beforeEach {
            every { fcnpcNativeFunctions.getWeaponShootTime(npcId, WeaponModel.M4.value) } returns 999
        }

        it("should return shoot time") {
            assertThat(weapons.getShootTime(WeaponModel.M4))
                    .isEqualTo(999)
        }
    }

    describe("setClipSize") {
        beforeEach {
            every { fcnpcNativeFunctions.setWeaponClipSize(any(), any(), any()) } returns true
            weapons.setClipSize(WeaponModel.M4, 999)
        }

        it("should call fcnpcNativeFunctions.setWeaponClipSize") {
            verify { fcnpcNativeFunctions.setWeaponClipSize(npcId, WeaponModel.M4.value, 999) }
        }
    }

    describe("getClipSize") {
        beforeEach {
            every { fcnpcNativeFunctions.getWeaponClipSize(npcId, WeaponModel.M4.value) } returns 999
        }

        it("should return clip size") {
            assertThat(weapons.getClipSize(WeaponModel.M4))
                    .isEqualTo(999)
        }
    }

    describe("getActualClipSize") {
        beforeEach {
            every { fcnpcNativeFunctions.getWeaponActualClipSize(npcId, WeaponModel.M4.value) } returns 999
        }

        it("should return actual clip size") {
            assertThat(weapons.getActualClipSize(WeaponModel.M4))
                    .isEqualTo(999)
        }
    }

    describe("setAccuracy") {
        beforeEach {
            every { fcnpcNativeFunctions.setWeaponAccuracy(any(), any(), any()) } returns true
            weapons.setAccuracy(WeaponModel.M4, 50f)
        }

        it("should call fcnpcNativeFunctions.setWeaponAccuracy") {
            verify { fcnpcNativeFunctions.setWeaponAccuracy(npcId, WeaponModel.M4.value, 50f) }
        }
    }

    describe("getAccuracy") {
        beforeEach {
            every { fcnpcNativeFunctions.getWeaponAccuracy(npcId, WeaponModel.M4.value) } returns 50f
        }

        it("should return  accuracy") {
            assertThat(weapons.getAccuracy(WeaponModel.M4))
                    .isEqualTo(50f)
        }
    }

    describe("setInfo") {
        describe("setter") {
            beforeEach {
                every { fcnpcNativeFunctions.setWeaponInfo(any(), any(), any(), any(), any(), any()) } returns true
            }

            context("all values are set") {
                beforeEach {
                    weapons.setInfo(
                            WeaponModel.M4,
                            WeaponInfo(
                                    reloadTime = 123,
                                    shootTime = 456,
                                    clipSize = 69,
                                    accuracy = 0.815f
                            )
                    )
                }

                it("should call fcnpcNativeFunctions.setWeaponInfo with input values") {
                    verify {
                        fcnpcNativeFunctions.setWeaponInfo(
                                npcid = npcId,
                                weaponid = WeaponModel.M4.value,
                                reload_time = 123,
                                shoot_time = 456,
                                clip_size = 69,
                                accuracy = 0.815f
                        )
                    }
                }
            }

            context("no values are set") {
                beforeEach {
                    weapons.setInfo(WeaponModel.M4, WeaponInfo())
                }

                it("should call fcnpcNativeFunctions.setWeaponInfo with default values") {
                    verify {
                        fcnpcNativeFunctions.setWeaponInfo(
                                npcid = npcId,
                                weaponid = WeaponModel.M4.value,
                                reload_time = -1,
                                shoot_time = -1,
                                clip_size = -1,
                                accuracy = 1f
                        )
                    }
                }
            }
        }
    }

    describe("getInfo") {
        beforeEach {
            every {
                fcnpcNativeFunctions.getWeaponInfo(
                        npcid = npcId,
                        weaponid = WeaponModel.MINIGUN.value,
                        reload_time = any(),
                        shoot_time = any(),
                        clip_size = any(),
                        accuracy = any()
                )
            } answers {
                thirdArg<MutableIntCell>().value = 123
                arg<MutableIntCell>(3).value = 456
                arg<MutableIntCell>(4).value = 789
                arg<MutableFloatCell>(5).value = 69f
                true
            }
        }

        it("should return weapon info") {
            assertThat(weapons.getInfo(WeaponModel.MINIGUN))
                    .isEqualTo(WeaponInfo(reloadTime = 123, shootTime = 456, clipSize = 789, accuracy = 69f))
        }
    }
})