package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object MovePathRegistryProviderSpec : Spek({

    val movePathRegistryProvider by memoized { MovePathRegistryProvider() }

    describe("get") {
        context("capacity is default value") {
            lateinit var movePathRegistry: MovePathRegistry

            beforeEach {
                movePathRegistry = movePathRegistryProvider.get()
            }

            it("should have default capacity") {
                assertThat(movePathRegistry.capacity)
                        .isEqualTo(65536)
            }
        }

        context("capacity is set") {
            lateinit var movePathRegistry: MovePathRegistry

            beforeEach {
                movePathRegistryProvider.registryCapacity = 1234
                movePathRegistry = movePathRegistryProvider.get()
            }

            it("should have default capacity") {
                assertThat(movePathRegistry.capacity)
                        .isEqualTo(1234)
            }
        }

        context("should return singleton instance") {
            lateinit var movePathRegistry1: MovePathRegistry
            lateinit var movePathRegistry2: MovePathRegistry

            beforeEach {
                movePathRegistry1 = movePathRegistryProvider.get()
                movePathRegistry2 = movePathRegistryProvider.get()
            }

            it("should have default capacity") {
                assertThat(movePathRegistry1)
                        .isSameAs(movePathRegistry2)
            }
        }
    }

})