package ch.leadrian.samp.kamp.fcnpcwrapper.service

import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.Node
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.NodeId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.NodeRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object NodeLoaderSpec : Spek({
    val nodeRegistry by memoized { mockk<NodeRegistry>() }
    val nativeFunctions by memoized { mockk<FCNPCNativeFunctions>(relaxed = true) }
    val nodeLoader by memoized { NodeLoader(nodeRegistry, nativeFunctions) }

    describe("load") {
        val nodeId = NodeId.valueOf(13)
        lateinit var node: Node

        context("node has not yet been loaded") {
            beforeEach {
                every { nodeRegistry.register(any()) } just Runs
                every { nodeRegistry[nodeId] } returns null
                node = nodeLoader.load(nodeId)
            }

            it("should create node with ID") {
                assertThat(node.id)
                        .isEqualTo(nodeId)
            }

            it("should register node") {
                verify { nodeRegistry.register(node) }
            }
        }

        context("node has already been loaded") {
            val expectedNode by memoized { mockk<Node>() }

            beforeEach {
                every { nodeRegistry[nodeId] } returns expectedNode
                node = nodeLoader.load(nodeId)
            }

            it("should return registered node") {
                assertThat(node)
                        .isEqualTo(expectedNode)
            }
        }
    }
})