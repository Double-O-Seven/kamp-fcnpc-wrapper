package ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object PlaybackRecordRegistryProviderSpec : Spek({

    val playbackRecordRegistryProvider by memoized { PlaybackRecordRegistryProvider() }

    describe("get") {
        context("capacity is default value") {
            lateinit var playbackRecordRegistry: PlaybackRecordRegistry

            beforeEach {
                playbackRecordRegistry = playbackRecordRegistryProvider.get()
            }

            it("should have default capacity") {
                assertThat(playbackRecordRegistry.capacity)
                        .isEqualTo(4096)
            }
        }

        context("capacity is set") {
            lateinit var playbackRecordRegistry: PlaybackRecordRegistry

            beforeEach {
                playbackRecordRegistryProvider.registryCapacity = 1234
                playbackRecordRegistry = playbackRecordRegistryProvider.get()
            }

            it("should have default capacity") {
                assertThat(playbackRecordRegistry.capacity)
                        .isEqualTo(1234)
            }
        }

        context("should return singleton instance") {
            lateinit var playbackRecordRegistry1: PlaybackRecordRegistry
            lateinit var playbackRecordRegistry2: PlaybackRecordRegistry

            beforeEach {
                playbackRecordRegistry1 = playbackRecordRegistryProvider.get()
                playbackRecordRegistry2 = playbackRecordRegistryProvider.get()
            }

            it("should have default capacity") {
                assertThat(playbackRecordRegistry1)
                        .isSameAs(playbackRecordRegistry2)
            }
        }
    }

})