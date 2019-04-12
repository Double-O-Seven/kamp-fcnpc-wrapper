package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathPointId
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object MovePathPointSpec : Spek({

    val movePathPointId = 69
    val movePathId = 1337
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    lateinit var movePath: MovePath
    lateinit var movePathPoint: MovePathPoint

    beforeEachTest {
        movePath = mockk {
            every { id } returns MovePathId.valueOf(movePathId)
        }
        movePathPoint = MovePathPoint(MovePathPointId.valueOf(movePathPointId), movePath, fcnpcNativeFunctions)
    }

    describe("isValid") {
        context("move path is destroyed") {
            beforeEach {
                every { movePath.isDestroyed } returns true
            }

            it("should return false") {
                assertThat(movePathPoint.isValid)
                        .isFalse()
            }
        }

        context("move path is not destroyed") {
            beforeEach {
                every { movePath.isDestroyed } returns false
            }

            context("point has not been removed") {
                it("should return true") {
                    assertThat(movePathPoint.isValid)
                            .isTrue()
                }
            }

            context("point has been removed") {
                beforeEach {
                    every { fcnpcNativeFunctions.removePointFromMovePath(any(), any()) } returns true
                    every { movePath.onRemove(any()) } just Runs
                    movePathPoint.removeFromPath()
                }

                it("should return false") {
                    assertThat(movePathPoint.isValid)
                            .isFalse()
                }
            }
        }
    }

    describe("coordinates") {
        beforeEach {
            every { fcnpcNativeFunctions.getMovePathPoint(movePathId, movePathPointId, any(), any(), any()) } answers {
                thirdArg<MutableFloatCell>().value = 1f
                arg<MutableFloatCell>(3).value = 2f
                arg<MutableFloatCell>(4).value = 3f
                true
            }
        }

        it("should return coordinates") {
            assertThat(movePathPoint.coordinates)
                    .isEqualTo(vector3DOf(1f, 2f, 3f))
        }
    }

    describe("removeFromPath") {
        beforeEach {
            every { fcnpcNativeFunctions.removePointFromMovePath(any(), any()) } returns true
            every { movePath.onRemove(any()) } just Runs
            movePathPoint.removeFromPath()
        }

        it("should call fcnpcNativeFunctions.removePointFromMovePath") {
            verify { fcnpcNativeFunctions.removePointFromMovePath(movePathId, movePathPointId) }
        }

        it("should call movePath.onRemove") {
            verify { movePath.onRemove(movePathPoint) }
        }

        context("second call") {
            beforeEach {
                movePathPoint.removeFromPath()
            }

            it("should not call fcnpcNativeFunctions.removePointFromMovePath again") {
                verify(exactly = 1) { fcnpcNativeFunctions.removePointFromMovePath(any(), any()) }
            }

            it("should not call movePath.onRemove again") {
                verify(exactly = 1) { movePath.onRemove(any()) }
            }
        }
    }

})