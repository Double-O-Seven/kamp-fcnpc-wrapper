package ch.leadrian.samp.kamp.fcnpcwrapper.entity.factory

import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.PlaybackRecord
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.registry.PlaybackRecordRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object PlaybackRecordFactorySpec : Spek({
    val playbackRecordRegistry by memoized { mockk<PlaybackRecordRegistry>() }
    val nativeFunctions by memoized { mockk<FCNPCNativeFunctions>(relaxed = true) }
    val playbackRecordFactory by memoized { PlaybackRecordFactory(playbackRecordRegistry, nativeFunctions) }

    describe("load") {
        lateinit var playbackRecord: PlaybackRecord
        val file = "my_npc.rec"

        beforeEach {
            every { playbackRecordRegistry.register(any()) } just Runs
            playbackRecord = playbackRecordFactory.load(file)
        }

        it("should create playback record with file") {
            assertThat(playbackRecord.file)
                    .isEqualTo(file)
        }

        it("should register playback record in registry") {
            verify { playbackRecordRegistry.register(playbackRecord) }
        }

        context("playbackRecord.onDestroy is called") {
            beforeEach {
                every { playbackRecordRegistry.unregister(any()) } just Runs
                playbackRecord.destroy()
            }

            it("should unregister playbackRecord in registry") {
                verify { playbackRecordRegistry.unregister(playbackRecord) }
            }
        }
    }

})