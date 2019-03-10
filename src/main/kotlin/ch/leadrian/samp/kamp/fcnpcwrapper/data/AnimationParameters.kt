package ch.leadrian.samp.kamp.fcnpcwrapper.data

sealed class AnimationParameters {

    abstract val fDelta: Float
    abstract val loop: Boolean
    abstract val lockX: Boolean
    abstract val lockY: Boolean
    abstract val freeze: Boolean
    abstract val time: Int

}

data class SimpleAnimationParameters
@JvmOverloads
constructor(
        override val fDelta: Float = 4.1f,
        override val loop: Boolean = false,
        override val lockX: Boolean = true,
        override val lockY: Boolean = true,
        override val freeze: Boolean = false,
        override val time: Int = 1
) : AnimationParameters()

data class NamedAnimationParameters
@JvmOverloads
constructor(
        val name: String,
        override val fDelta: Float = 4.1f,
        override val loop: Boolean = false,
        override val lockX: Boolean = true,
        override val lockY: Boolean = true,
        override val freeze: Boolean = false,
        override val time: Int = 1
) : AnimationParameters()

data class AnimationWithIdParameters
@JvmOverloads
constructor(
        val id: Int,
        override val fDelta: Float = 4.1f,
        override val loop: Boolean = false,
        override val lockX: Boolean = true,
        override val lockY: Boolean = true,
        override val freeze: Boolean = false,
        override val time: Int = 1
) : AnimationParameters()