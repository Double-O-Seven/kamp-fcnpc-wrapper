package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.MovePathPointId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object MovePathSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }


    describe("constructor") {
        context("move path ID is valid") {
            val movePathId = 1337
            lateinit var movePath: MovePath

            beforeEachTest {
                every { fcnpcNativeFunctions.createMovePath() } returns movePathId
                movePath = MovePath(fcnpcNativeFunctions)
            }

            it("should initialize with move path ID") {
                assertThat(movePath.id.value)
                        .isEqualTo(movePathId)
            }
        }

        context("move path ID is INVALID_MOVEPATH_ID") {
            lateinit var caughtThrowable: Throwable

            beforeEachTest {
                every { fcnpcNativeFunctions.createMovePath() } returns FCNPCConstants.FCNPC_INVALID_MOVEPATH_ID
                caughtThrowable = catchThrowable { MovePath(fcnpcNativeFunctions) }
            }

            it("should throw exception") {
                assertThat(caughtThrowable)
                        .isInstanceOf(CreationFailedException::class.java)
                        .hasMessage("Could not create move path")
            }
        }
    }

    describe("constructed instance") {
        val movePathId = 1337
        lateinit var movePath: MovePath

        beforeEachTest {
            every { fcnpcNativeFunctions.createMovePath() } returns movePathId
            movePath = MovePath(fcnpcNativeFunctions)
        }

        describe("addPoint") {
            val movePathPointId = MovePathPointId.valueOf(1337)
            lateinit var movePathPoint: MovePathPoint

            beforeEach {
                every { fcnpcNativeFunctions.addPointToMovePath(movePathId, 1f, 2f, 3f) } returns movePathPointId.value
                movePathPoint = movePath.addPoint(vector3DOf(1f, 2f, 3f))
            }

            it("should return MovePathPoint") {
                assertThat(movePathPoint.id)
                        .isEqualTo(movePathPointId)
            }

            it("should add point to points") {
                assertThat(movePath.points)
                        .containsExactlyInAnyOrder(movePathPoint)
            }

            context("another point is added") {
                val movePathPointId2 = MovePathPointId.valueOf(187)
                lateinit var movePathPoint2: MovePathPoint

                beforeEach {
                    every {
                        fcnpcNativeFunctions.addPointToMovePath(
                                movePathId,
                                4f,
                                5f,
                                6f
                        )
                    } returns movePathPointId2.value
                    movePathPoint2 = movePath.addPoint(vector3DOf(4f, 5f, 6f))
                }

                it("should add point to points") {
                    assertThat(movePath.points)
                            .containsExactlyInAnyOrder(movePathPoint, movePathPoint2)
                }
            }
        }

        describe("onRemove") {
            val movePathPointId = MovePathPointId.valueOf(1337)
            lateinit var movePathPoint: MovePathPoint

            beforeEach {
                every { fcnpcNativeFunctions.addPointToMovePath(movePathId, 1f, 2f, 3f) } returns movePathPointId.value
                movePathPoint = movePath.addPoint(vector3DOf(1f, 2f, 3f))
                movePath.onRemove(movePathPoint)
            }

            it("should remove point from points") {
                assertThat(movePath.points)
                        .isEmpty()
            }
        }

        describe("onDestroy") {
            beforeEach {
                every { fcnpcNativeFunctions.destroyMovePath(any()) } returns true
                movePath.destroy()
            }

            it("should call fcnpcNativeFunctions.destroyMovePath") {
                verify { fcnpcNativeFunctions.destroyMovePath(movePathId) }
            }
        }
    }
})