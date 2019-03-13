package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object PlaybackRecordSpec : Spek({

    val nativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }

    describe("init") {
        val file = "my_npc.rec"

        context("loadPlayingPlayback returns valid record ID") {
            val recordId = 69
            lateinit var playbackRecord: PlaybackRecord

            beforeEach {
                every { nativeFunctions.loadPlayingPlayback(file) } returns recordId
                playbackRecord = PlaybackRecord(file, nativeFunctions)
            }

            it("should initialize playback record file") {
                assertThat(playbackRecord.file)
                        .isEqualTo(file)
            }

            it("should playback record ID") {
                assertThat(playbackRecord.id.value)
                        .isEqualTo(recordId)
            }
        }

        context("loadPlayingPayback returns INVALID_RECORD_ID") {
            lateinit var caughtThrowable: Throwable

            beforeEach {
                every { nativeFunctions.loadPlayingPlayback(file) } returns FCNPCConstants.FCNPC_INVALID_RECORD_ID
                caughtThrowable = catchThrowable { PlaybackRecord(file, nativeFunctions) }
            }

            it("should throw CreationFailedException") {
                assertThat(caughtThrowable)
                        .isInstanceOf(CreationFailedException::class.java)
                        .hasMessage("Could not load record from file: $file")
            }
        }
    }

    describe("created instance") {
        val recordId = 69
        lateinit var playbackRecord: PlaybackRecord

        beforeEach {
            every { nativeFunctions.loadPlayingPlayback(any()) } returns recordId
            playbackRecord = PlaybackRecord("my_npc.rec", nativeFunctions)
        }

        describe("destroy") {
            beforeEach {
                every { nativeFunctions.unloadPlayingPlayback(any()) } returns true
                playbackRecord.destroy()
            }

            it("should call nativeFunctions.unloadPlayingPlayback") {
                verify { nativeFunctions.unloadPlayingPlayback(recordId) }
            }
        }
    }
})