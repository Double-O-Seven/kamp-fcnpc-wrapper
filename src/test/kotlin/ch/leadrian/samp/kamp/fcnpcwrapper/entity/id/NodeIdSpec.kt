package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object NodeIdSpec : Spek({
    describe("valueOf") {
        listOf(-1, 0, 63, 64).forEach { value ->
            it("should return NodeId with value $value") {
                val nodeId = NodeId.valueOf(value)

                assertThat(nodeId.value)
                        .isEqualTo(value)
            }
        }

        listOf(0, 63).forEach { value ->
            it("should used cached NodeId for value $value") {
                val nodeId = NodeId.valueOf(value)

                assertThat(nodeId)
                        .isSameAs(NodeId.valueOf(value))
            }
        }

        listOf(-1, 64).forEach { value ->
            it("should create new NodeId for value $value") {
                val nodeId = NodeId.valueOf(value)

                assertThat(nodeId)
                        .isNotSameAs(NodeId.valueOf(value))
            }
        }
    }
})