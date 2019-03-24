package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.MutableFloatCell
import ch.leadrian.samp.kamp.core.api.amx.MutableIntCell
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveMode
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveSpeed
import ch.leadrian.samp.kamp.fcnpcwrapper.constants.MoveType
import ch.leadrian.samp.kamp.fcnpcwrapper.data.NodeInfo
import ch.leadrian.samp.kamp.fcnpcwrapper.data.PlayNodeParameters
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import ch.leadrian.samp.kamp.fcnpcwrapper.exception.NodeNotOpenException
import ch.leadrian.samp.kamp.fcnpcwrapper.exception.OpenNodeFailedException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object NodeSpec : Spek({
    describe("init") {
        listOf(0, FCNPCConstants.FCNPC_MAX_NODES - 1).forEach { nodeId ->
            context("node ID is $nodeId") {
                lateinit var node: Node
                beforeEach {
                    node = Node(NodeId.valueOf(nodeId), mockk())
                }

                it("should set node ID to $nodeId") {
                    assertThat(node.id.value)
                            .isEqualTo(nodeId)
                }
            }
        }

        listOf(-1, FCNPCConstants.FCNPC_MAX_NODES).forEach { nodeId ->
            context("node ID is $nodeId") {
                var caughtThrowable: Throwable? = null
                beforeEach {
                    caughtThrowable = catchThrowable { Node(NodeId.valueOf(nodeId), mockk()) }
                }

                it("should set node ID to $nodeId") {
                    assertThat(caughtThrowable)
                            .isInstanceOf(IllegalArgumentException::class.java)
                            .hasMessage("Invalid node ID: $nodeId")
                }
            }
        }
    }

    describe("created instance") {
        val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
        val nodeId = 32
        val node by memoized { Node(NodeId.valueOf(nodeId), fcnpcNativeFunctions) }

        describe("isOpen") {
            listOf(true, false).forEach { isOpen ->
                context("fcnpcNativeFunctions.isNodeOpen returns $isOpen") {
                    beforeEach {
                        every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns isOpen
                    }

                    it("should return $isOpen") {
                        assertThat(node.isOpen)
                                .isEqualTo(isOpen)
                    }
                }
            }
        }

        describe("type") {
            beforeEach {
                every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns true
                every { fcnpcNativeFunctions.getNodeType(nodeId) } returns 69
            }

            it("should return node type") {
                assertThat(node.type)
                        .isEqualTo(69)
            }
        }

        describe("pointCount") {
            beforeEach {
                every { fcnpcNativeFunctions.getNodePointCount(nodeId) } returns 69
            }

            it("should return node point count") {
                assertThat(node.pointCount)
                        .isEqualTo(69)
            }
        }

        describe("pointCoordinates") {
            beforeEach {
                every { fcnpcNativeFunctions.getNodePointPosition(nodeId, any(), any(), any()) } answers {
                    secondArg<MutableFloatCell>().value = 1f
                    thirdArg<MutableFloatCell>().value = 2f
                    arg<MutableFloatCell>(3).value = 3f
                    true
                }
            }

            it("should return coordinates") {
                assertThat(node.pointCoordinates)
                        .isEqualTo(vector3DOf(1f, 2f, 3f))
            }
        }

        describe("info") {
            beforeEach {
                every { fcnpcNativeFunctions.getNodeInfo(nodeId, any(), any(), any()) } answers {
                    secondArg<MutableIntCell>().value = 111
                    thirdArg<MutableIntCell>().value = 222
                    arg<MutableIntCell>(3).value = 333
                    true
                }
            }

            it("should return info") {
                assertThat(node.info)
                        .isEqualTo(
                                NodeInfo(
                                        numberOfVehicleNodes = 111,
                                        numberOfPedNodes = 222,
                                        numberOfNaviNodes = 333
                                )
                        )
            }
        }

        describe("open") {
            context("node is already open") {
                beforeEach {
                    every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns true
                    every { fcnpcNativeFunctions.openNode(any()) } returns true
                }

                it("should not call fcnpcNativeFunctions.openNode") {
                    verify(exactly = 0) { fcnpcNativeFunctions.openNode(any()) }
                }
            }

            context("node is not yet open") {
                beforeEach {
                    every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns false
                }

                context("fcnpcNativeFunctions.openNode returns true") {
                    beforeEach {
                        every { fcnpcNativeFunctions.openNode(any()) } returns true
                        node.open()
                    }

                    it("should call fcnpcNativeFunctions.openNode") {
                        verify { fcnpcNativeFunctions.openNode(nodeId) }
                    }
                }

                context("fcnpcNativeFunctions.openNode returns false") {
                    var caughtThrowable: Throwable? = null

                    beforeEach {
                        every { fcnpcNativeFunctions.openNode(any()) } returns false
                        caughtThrowable = catchThrowable { node.open() }
                    }

                    it("should throw exception") {
                        assertThat(caughtThrowable)
                                .isInstanceOf(OpenNodeFailedException::class.java)
                                .hasMessage("Failed to open node $nodeId")
                    }
                }
            }
        }

        describe("close") {
            beforeEach {
                every { fcnpcNativeFunctions.closeNode(any()) } returns true
                node.close()
            }

            it("should call fcnpcNativeFunctions.closeNode") {
                verify { fcnpcNativeFunctions.closeNode(nodeId) }
            }
        }

        describe("setPoint") {
            beforeEach {
                every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns true
            }

            listOf(0, 499).forEach { pointId ->
                context("node point ID $pointId is valid") {
                    beforeEach {
                        every { fcnpcNativeFunctions.getNodePointCount(nodeId) } returns 500
                        every { fcnpcNativeFunctions.setNodePoint(any(), any()) } returns true
                        node.setPoint(pointId)
                    }

                    it("should call fcnpcNativeFunctions.setNodePoint") {
                        verify { fcnpcNativeFunctions.setNodePoint(nodeid = nodeId, pointid = pointId) }
                    }
                }
            }

            listOf(-1, 500).forEach { pointId ->
                context("node point ID $pointId is not valid") {
                    var caughtThrowable: Throwable? = null

                    beforeEach {
                        every { fcnpcNativeFunctions.getNodePointCount(nodeId) } returns 500
                        every { fcnpcNativeFunctions.setNodePoint(any(), any()) } returns true
                        caughtThrowable = catchThrowable { node.setPoint(pointId) }
                    }

                    it("should call fcnpcNativeFunctions.setNodePoint") {
                        assertThat(caughtThrowable)
                                .isInstanceOf(IllegalArgumentException::class.java)
                                .hasMessage("point ID $pointId is not a valid point for node $nodeId with 500 points")
                    }
                }
            }
        }

        describe("play") {
            val npcId = 187
            val npc by memoized {
                mockk<FullyControllableNPC> {
                    every { id } returns FullyControllableNPCId.valueOf(npcId)
                }
            }

            beforeEach {
                every { fcnpcNativeFunctions.playNode(any(), any(), any(), any(), any(), any(), any()) } returns true
                every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns true
            }

            context("all arguments defined") {
                beforeEach {
                    val parameters = PlayNodeParameters(
                            MoveType.SPRINT,
                            MoveSpeed.WALK,
                            MoveMode.MAP_ANDREAS,
                            13.37f,
                            false
                    )
                    node.play(npc, parameters)
                }

                it("should call fcnpcNativeFunctions.playNode") {
                    fcnpcNativeFunctions.playNode(
                            npcid = npcId,
                            nodeid = nodeId,
                            move_type = MoveType.SPRINT.value,
                            speed = MoveSpeed.WALK.value,
                            mode = MoveMode.MAP_ANDREAS.value,
                            radius = 13.37f,
                            set_angle = false
                    )
                }
            }

            context("minimal arguments are defined") {
                beforeEach {
                    node.play(npc)
                }

                it("should call fcnpcNativeFunctions.playNode") {
                    fcnpcNativeFunctions.playNode(
                            npcid = npcId,
                            nodeid = nodeId,
                            move_type = MoveType.AUTO.value,
                            speed = MoveSpeed.AUTO.value,
                            mode = MoveMode.AUTO.value,
                            radius = 0f,
                            set_angle = true
                    )
                }
            }
        }

        describe("requireOpen") {
            context("without action") {
                context("fcnpcNativeFunctions.isNodeOpen returns true") {
                    var caughtThrowable: Throwable? = null

                    beforeEach {
                        every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns true
                        caughtThrowable = catchThrowable { node.requireOpen() }
                    }

                    it("should not throw exception") {
                        assertThat(caughtThrowable)
                                .isNull()
                    }
                }

                context("fcnpcNativeFunctions.isNodeOpen returns false") {
                    var caughtThrowable: Throwable? = null

                    beforeEach {
                        every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns false
                        caughtThrowable = catchThrowable { node.requireOpen() }
                    }

                    it("should not throw exception") {
                        assertThat(caughtThrowable)
                                .isInstanceOf(NodeNotOpenException::class.java)
                                .hasMessage("Node $nodeId is not open")
                    }
                }
            }

            context("with action") {
                val action by memoized { mockk<Node.() -> String>(relaxed = true) }

                context("fcnpcNativeFunctions.isNodeOpen returns true") {
                    var caughtThrowable: Throwable? = null

                    beforeEach {
                        every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns true
                        caughtThrowable = catchThrowable { node.requireOpen(action) }
                    }

                    it("should not throw exception") {
                        assertThat(caughtThrowable)
                                .isNull()
                    }

                    it("should call action") {
                        verify { action(node) }
                    }
                }

                context("fcnpcNativeFunctions.isNodeOpen returns false") {
                    var caughtThrowable: Throwable? = null

                    beforeEach {
                        every { fcnpcNativeFunctions.isNodeOpen(nodeId) } returns false
                        caughtThrowable = catchThrowable { node.requireOpen(action) }
                    }

                    it("should not throw exception") {
                        assertThat(caughtThrowable)
                                .isInstanceOf(NodeNotOpenException::class.java)
                                .hasMessage("Node $nodeId is not open")
                    }

                    it("should not call action") {
                        verify(exactly = 0) { action(any()) }
                    }
                }
            }
        }
    }
})