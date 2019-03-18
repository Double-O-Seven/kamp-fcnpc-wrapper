package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MovePathFinding
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveSpeed
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveType
import ch.leadrian.samp.kamp.fcnpcwrapper.data.GoToParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.Arrays
import java.util.function.Function
import java.util.stream.Stream
import kotlin.streams.toList

internal object MovementSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val npcId = 123
    val npc by memoized {
        mockk<FullyControllableNPC> {
            every { id } returns FullyControllableNPCId.valueOf(npcId)
        }
    }
    val movement by memoized { Movement(npc, fcnpcNativeFunctions) }

    describe("isMoving") {
        listOf(true, false).forEach { isMoving ->
            context("fcnpcNativeFunctions.isMoving returns $isMoving") {
                beforeEach {
                    every { fcnpcNativeFunctions.isMoving(npcId) } returns isMoving
                }

                it("should return $isMoving") {
                    assertThat(movement.isMoving)
                            .isEqualTo(isMoving)
                }
            }
        }
    }

    describe("destinationPoint") {
        beforeEach {
            every { fcnpcNativeFunctions.getDestinationPoint(npcId, any(), any(), any()) } answers {
                secondArg<MutableFloatCell>().value = 1f
                thirdArg<MutableFloatCell>().value = 2f
                arg<MutableFloatCell>(3).value = 3f
                true
            }
        }

        it("should return coordinates") {
            assertThat(movement.destinationPoint)
                    .isEqualTo(vector3DOf(1f, 2f, 3f))
        }
    }

    describe("goto") {
        context("target are coordinates") {
            beforeEach {
                every {
                    fcnpcNativeFunctions.goTo(
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

            getGoToParameters().forEach { parameters ->
                context("parameters are set to $parameters") {
                    beforeEach {
                        movement.goTo(vector3DOf(1f, 2f, 3f), parameters)
                    }

                    it("should call fcnpcNativeFunctions.goTo") {
                        verify {
                            fcnpcNativeFunctions.goTo(
                                    npcid = npcId,
                                    x = 1f,
                                    y = 2f,
                                    z = 3f,
                                    type = parameters.type.value,
                                    speed = parameters.speed.value,
                                    mode = parameters.mode.value,
                                    pathfinding = parameters.pathFinding.value,
                                    radius = parameters.radius,
                                    set_angle = parameters.setAngle,
                                    min_distance = parameters.minDistance,
                                    stopdelay = parameters.stopDelay
                            )
                        }
                    }
                }
            }
        }

        context("target is player") {
            val playerId = 1234
            val player by memoized {
                mockk<Player> {
                    every { id } returns PlayerId.valueOf(playerId)
                }
            }

            beforeEach {
                every {
                    fcnpcNativeFunctions.goToPlayer(
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

            getGoToParameters().forEach { parameters ->
                context("parameters are set to $parameters") {
                    beforeEach {
                        movement.goTo(player, parameters)
                    }

                    it("should call fcnpcNativeFunctions.goTo") {
                        verify {
                            fcnpcNativeFunctions.goToPlayer(
                                    npcid = npcId,
                                    playerid = playerId,
                                    type = parameters.type.value,
                                    speed = parameters.speed.value,
                                    mode = parameters.mode.value,
                                    pathfinding = parameters.pathFinding.value,
                                    radius = parameters.radius,
                                    set_angle = parameters.setAngle,
                                    min_distance = parameters.minDistance,
                                    dist_check = parameters.distanceCheck,
                                    stopdelay = parameters.stopDelay
                            )
                        }
                    }
                }
            }
        }
    }

    describe("stop") {
        beforeEach {
            every { fcnpcNativeFunctions.stop(any()) } returns true
            movement.stop()
        }

        it("should call fcnpcNativeFunctions.stop") {
            verify { fcnpcNativeFunctions.stop(npcId) }
        }
    }

    describe("isMovingTo") {
        val playerId = 123
        val player by memoized {
            mockk<Player> {
                every { id } returns PlayerId.valueOf(playerId)
            }
        }
        listOf(true, false).forEach { isMoving ->
            context("fcnpcNativeFunctions.isMovingAtPlayer returns $isMoving") {
                beforeEach {
                    every { fcnpcNativeFunctions.isMovingAtPlayer(npcId, playerId) } returns isMoving
                }

                it("should return $isMoving") {
                    assertThat(movement.isMovingTo(player))
                            .isEqualTo(isMoving)
                }
            }
        }
    }
})

fun getGoToParameters(): List<GoToParameters> {
    return Stream
            .of(
                    Arrays.stream(MoveType.values()).map { GoToParameters().copy(type = it) },
                    Arrays.stream(MoveSpeed.values()).map { GoToParameters().copy(speed = it) },
                    Arrays.stream(MoveMode.values()).map { GoToParameters().copy(mode = it) },
                    Arrays.stream(MovePathFinding.values()).map { GoToParameters().copy(pathFinding = it) },
                    Stream.of(true, false).map { GoToParameters().copy(setAngle = it) }
            )
            .flatMap(Function.identity())
            .toList()
}