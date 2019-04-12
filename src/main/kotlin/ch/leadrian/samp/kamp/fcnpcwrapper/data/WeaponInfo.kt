package ch.leadrian.samp.kamp.fcnpcwrapper.data

data class WeaponInfo
@JvmOverloads
constructor(
        val reloadTime: Int? = null,
        val shootTime: Int? = null,
        val clipSize: Int? = null,
        val accuracy: Float = 1f
) {

    companion object {

        val DEFAULT = WeaponInfo()

    }

}