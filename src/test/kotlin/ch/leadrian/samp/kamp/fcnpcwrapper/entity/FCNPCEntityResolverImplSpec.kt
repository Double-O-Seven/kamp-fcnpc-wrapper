package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.FullyControllableNPCRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.MovePathRegistry
import ch.leadrian.samp.kamp.fcnpcwrapper.service.NodeLoader
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class FCNPCEntityResolverImplSpec : Spek({
    val fullyControllableNPCRegistry by memoized { mockk<FullyControllableNPCRegistry>() }
    val nodeLoader by memoized { mockk<NodeLoader>() }
    val movePathRegistry by memoized { mockk<MovePathRegistry>() }
    val fcnpcEntityResolver by memoized {
        FCNPCEntityResolverImpl(fullyControllableNPCRegistry, nodeLoader, movePathRegistry, mockk())
    }

    describe("toNPC") {
        val npcId = 1337

        context("npc ID is valid") {
            val expectedNPC by memoized { mockk<FullyControllableNPC>() }
            lateinit var npc: FullyControllableNPC

            beforeEach {
                every { fullyControllableNPCRegistry[npcId] } returns expectedNPC
                npc = fcnpcEntityResolver.run { npcId.toNPC() }
            }

            it("should return NPC") {
                assertThat(npc)
                        .isEqualTo(expectedNPC)
            }
        }

        context("npc ID is not valid") {
            lateinit var caughtThrowable: Throwable

            beforeEach {
                every { fullyControllableNPCRegistry[npcId] } returns null
                caughtThrowable = catchThrowable { fcnpcEntityResolver.run { npcId.toNPC() } }
            }

            it("should throw exception") {
                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid NPC ID 1337")
            }
        }
    }

    describe("toNode") {
        val npcId = 1337
        val npc by memoized { mockk<FullyControllableNPC>() }
        val nodeId = 69
        val expectedNode by memoized { mockk<Node>() }
        lateinit var node: Node

        beforeEach {
            every { fcnpcEntityResolver.run { npcId.toNPC() } } returns npc
            every { nodeLoader.load(NodeId.valueOf(nodeId)) } returns expectedNode
            node = fcnpcEntityResolver.run { nodeId.toNode() }
        }

        it("should return node") {
            assertThat(node)
                    .isEqualTo(expectedNode)
        }
    }

    describe("toMovePath") {
        val movePathId = 1337

        context("movePath ID is valid") {
            val expectedMovePath by memoized { mockk<MovePath>() }
            lateinit var movePath: MovePath

            beforeEach {
                every { movePathRegistry[movePathId] } returns expectedMovePath
                movePath = fcnpcEntityResolver.run { movePathId.toMovePath() }
            }

            it("should return move path") {
                assertThat(movePath)
                        .isEqualTo(expectedMovePath)
            }
        }

        context("movePath ID is not valid") {
            lateinit var caughtThrowable: Throwable

            beforeEach {
                every { movePathRegistry[movePathId] } returns null
                caughtThrowable = catchThrowable { fcnpcEntityResolver.run { movePathId.toMovePath() } }
            }

            it("should throw exception") {
                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid move path ID 1337")
            }
        }
    }
})