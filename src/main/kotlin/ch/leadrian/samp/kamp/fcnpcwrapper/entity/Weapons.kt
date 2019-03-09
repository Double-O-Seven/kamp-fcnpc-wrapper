package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponSkill
import ch.leadrian.samp.kamp.core.api.constants.WeaponState
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.data.WeaponInfo

class Weapons internal constructor(
        override val npc: FullyControllableNPC,
        private val nativeFunctions: FCNPCNativeFunctions
) : HasFullyControllableNPC {

    var current: WeaponModel
        get() = WeaponModel[nativeFunctions.getWeapon(npc.id.value)]
        set(value) {
            nativeFunctions.setWeapon(npcid = npc.id.value, weaponid = value.value)
        }

    var ammo: Int
        get() = nativeFunctions.getAmmo(npc.id.value)
        set(value) {
            nativeFunctions.setAmmo(npcid = npc.id.value, ammo = value)
        }

    var ammoInClip: Int
        get() = nativeFunctions.getAmmoInClip(npc.id.value)
        set(value) {
            nativeFunctions.setAmmoInClip(npcid = npc.id.value, ammo = value)
        }

    var state: WeaponState
        get() = WeaponState[nativeFunctions.getWeaponState(npc.id.value)]
        set(value) {
            nativeFunctions.setWeaponState(npcid = npc.id.value, weaponstate = value.value)
        }

    fun giveAmmo(ammo: Int) {
        nativeFunctions.giveAmmo(npcid = npc.id.value, ammo = ammo)
    }

    fun giveAmmoInClip(ammo: Int) {
        nativeFunctions.giveAmmoInClip(npcid = npc.id.value, ammo = ammo)
    }

    fun setSkillLevel(skill: WeaponSkill, level: Int) {
        nativeFunctions.setWeaponSkillLevel(npcid = npc.id.value, skill = skill.value, level = level)
    }

    fun giveSkillLevel(skill: WeaponSkill, level: Int) {
        nativeFunctions.giveWeaponSkillLevel(npcid = npc.id.value, skill = skill.value, level = level)
    }

    fun getSkillLevel(skill: WeaponSkill): Int =
            nativeFunctions.getWeaponSkillLevel(npcid = npc.id.value, skill = skill.value)

    fun setReloadTime(weapon: WeaponModel, time: Int) {
        nativeFunctions.setWeaponReloadTime(npcid = npc.id.value, weaponid = weapon.value, time = time)
    }

    fun getReloadTime(weapon: WeaponModel): Int =
            nativeFunctions.getWeaponReloadTime(npcid = npc.id.value, weaponid = weapon.value)

    fun getActualReloadTime(weapon: WeaponModel): Int =
            nativeFunctions.getWeaponActualReloadTime(npcid = npc.id.value, weaponid = weapon.value)

    fun setShootTime(weapon: WeaponModel, time: Int) {
        nativeFunctions.setWeaponShootTime(npcid = npc.id.value, weaponid = weapon.value, time = time)
    }

    fun getShootTime(weapon: WeaponModel): Int =
            nativeFunctions.getWeaponShootTime(npcid = npc.id.value, weaponid = weapon.value)

    fun setClipSize(weapon: WeaponModel, size: Int) {
        nativeFunctions.setWeaponClipSize(npcid = npc.id.value, weaponid = weapon.value, size = size)
    }

    fun getClipSize(weapon: WeaponModel): Int =
            nativeFunctions.getWeaponClipSize(npcid = npc.id.value, weaponid = weapon.value)

    fun getActualClipSize(weapon: WeaponModel): Int =
            nativeFunctions.getWeaponActualClipSize(npcid = npc.id.value, weaponid = weapon.value)

    fun setAccuracy(weapon: WeaponModel, accuracy: Float) {
        nativeFunctions.setWeaponAccuracy(npcid = npc.id.value, weaponid = weapon.value, accuracy = accuracy)
    }

    fun getAccuracy(weapon: WeaponModel): Float =
            nativeFunctions.getWeaponAccuracy(npcid = npc.id.value, weaponid = weapon.value)

    fun setInfo(weapon: WeaponModel, info: WeaponInfo) {
        nativeFunctions.setWeaponInfo(
                npcid = npc.id.value,
                weaponid = weapon.value,
                reload_time = info.reloadTime ?: -1,
                shoot_time = info.shootTime ?: -1,
                clip_size = info.clipSize ?: -1,
                accuracy = info.accuracy
        )
    }

    fun getInfo(weapon: WeaponModel): WeaponInfo {
        val reloadTime = MutableIntCell()
        val shootTime = MutableIntCell()
        val clipSize = MutableIntCell()
        val accuracy = MutableFloatCell()
        nativeFunctions.getWeaponInfo(
                npcid = npc.id.value,
                weaponid = weapon.value,
                reload_time = reloadTime,
                shoot_time = shootTime,
                clip_size = clipSize,
                accuracy = accuracy
        )
        return WeaponInfo(
                reloadTime = reloadTime.value,
                shootTime = shootTime.value,
                clipSize = clipSize.value,
                accuracy = accuracy.value
        )
    }

}