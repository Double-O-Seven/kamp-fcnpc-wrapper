package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableBooleanCell
import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.data.animationOf
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.data.AnimationWithIdParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.data.NamedAnimationParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.data.SimpleAnimationParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FCNPCAnimationSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val npcId = 123
    val npc by memoized {
        mockk<FullyControllableNPC> {
            every { id } returns FullyControllableNPCId.valueOf(npcId)
        }
    }
    val animation by memoized { FCNPCAnimation(npc, fcnpcNativeFunctions) }

    describe("set") {
        AnimationFlags.allCombinations.forEach { (loop, lockX, lockY, freeze) ->
            context("animation with ID with loop to $loop, lockX to $lockX, lockY to $lockY, freeze to $freeze") {
                beforeEach {
                    every {
                        fcnpcNativeFunctions.setAnimation(
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
                    animation.set(
                            AnimationWithIdParameters(
                                    id = 123,
                                    fDelta = 0.5f,
                                    loop = loop,
                                    lockX = lockX,
                                    lockY = lockY,
                                    freeze = freeze,
                                    time = 69
                            )
                    )
                }

                it("should call fcnpcNativeFunctions.setAnimation") {
                    verify {
                        fcnpcNativeFunctions.setAnimation(
                                npcid = npcId,
                                animationid = 123,
                                fDelta = 0.5f,
                                loop = loop,
                                lockx = lockX,
                                locky = lockY,
                                freeze = freeze,
                                time = 69
                        )
                    }
                }
            }

            context("animation with name with loop to $loop, lockX to $lockX, lockY to $lockY, freeze to $freeze") {
                beforeEach {
                    every {
                        fcnpcNativeFunctions.setAnimationByName(
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
                    animation.set(
                            NamedAnimationParameters(
                                    name = "amazing_anim",
                                    fDelta = 0.5f,
                                    loop = loop,
                                    lockX = lockX,
                                    lockY = lockY,
                                    freeze = freeze,
                                    time = 69
                            )
                    )
                }

                it("should call fcnpcNativeFunctions.setAnimationByName") {
                    verify {
                        fcnpcNativeFunctions.setAnimationByName(
                                npcid = npcId,
                                name = "amazing_anim",
                                fDelta = 0.5f,
                                loop = loop,
                                lockx = lockX,
                                locky = lockY,
                                freeze = freeze,
                                time = 69
                        )
                    }
                }
            }
        }
    }

    describe("reset") {
        beforeEach {
            every { fcnpcNativeFunctions.resetAnimation(any()) } returns true
            animation.reset()
        }

        it("should call fcnpcNativeFunctions.resetAnimation") {
            verify { fcnpcNativeFunctions.resetAnimation(npcId) }
        }
    }

    describe("get") {
        AnimationFlags.allCombinations.forEach { (loop, lockX, lockY, freeze) ->
            context("animation with loop to $loop, lockX to $lockX, lockY to $lockY, freeze to $freeze") {
                beforeEach {
                    every {
                        fcnpcNativeFunctions.getAnimation(
                                npcid = npcId,
                                animationid = any(),
                                fDelta = any(),
                                loop = any(),
                                lockx = any(),
                                locky = any(),
                                freeze = any(),
                                time = any()
                        )
                    } answers {
                        secondArg<MutableIntCell>().value = 1234
                        thirdArg<MutableFloatCell>().value = 1.337f
                        arg<MutableBooleanCell>(3).value = loop
                        arg<MutableBooleanCell>(4).value = lockX
                        arg<MutableBooleanCell>(5).value = lockY
                        arg<MutableBooleanCell>(6).value = freeze
                        arg<MutableIntCell>(7).value = 69
                        true
                    }
                }

                it("should return AnimationWithIdParameters") {
                    assertThat(animation.get())
                            .isEqualTo(
                                    AnimationWithIdParameters(
                                            id = 1234,
                                            fDelta = 1.337f,
                                            loop = loop,
                                            lockX = lockX,
                                            lockY = lockY,
                                            freeze = freeze,
                                            time = 69
                                    )
                            )
                }
            }
        }
    }

    describe("apply") {
        AnimationFlags.allCombinations.forEach { (loop, lockX, lockY, freeze) ->
            context("animation with loop to $loop, lockX to $lockX, lockY to $lockY, freeze to $freeze") {
                beforeEach {
                    every {
                        fcnpcNativeFunctions.applyAnimation(
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
                    animation.apply(
                            animationOf(library = "test", name = "animname"),
                            SimpleAnimationParameters(
                                    fDelta = 1.337f,
                                    loop = loop,
                                    lockX = lockX,
                                    lockY = lockY,
                                    freeze = freeze,
                                    time = 69
                            )
                    )
                }

                it("should call fcnpcNativeFunctions.applyAnimation") {
                    verify {
                        fcnpcNativeFunctions.applyAnimation(
                                npcid = npcId,
                                animlib = "test",
                                animname = "animname",
                                fDelta = 1.337f,
                                loop = loop,
                                lockx = lockX,
                                locky = lockY,
                                freeze = freeze,
                                time = 69
                        )
                    }
                }
            }
        }
    }

    describe("clear") {
        beforeEach {
            every { fcnpcNativeFunctions.clearAnimations(any()) } returns true
            animation.clear()
        }

        it("should call fcnpcNativeFunctions.clearAnimations") {
            verify { fcnpcNativeFunctions.clearAnimations(npcId) }
        }
    }
})

private data class AnimationFlags(val loop: Boolean, val lockX: Boolean, val lockY: Boolean, val freeze: Boolean) {

    companion object {

        @Suppress("BooleanLiteralArgument")
        val allCombinations: List<AnimationFlags>
            get() {
                val combinations = mutableListOf<AnimationFlags>()
                listOf(true, false).forEach { loop ->
                    listOf(true, false).forEach { lockX ->
                        listOf(true, false).forEach { lockY ->
                            listOf(true, false).forEach { freeze ->
                                combinations.add(
                                        AnimationFlags(
                                                loop = loop,
                                                lockX = lockX,
                                                lockY = lockY,
                                                freeze = freeze
                                        )
                                )
                            }
                        }
                    }
                }
                return combinations
            }

    }

}