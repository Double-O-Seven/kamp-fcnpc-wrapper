package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
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

    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }

    describe("init") {
        val file = "my_npc.rec"

        context("loadPlayingPlayback returns valid record ID") {
            val recordId = 69
            lateinit var playbackRecord: PlaybackRecord

            beforeEach {
                every { fcnpcNativeFunctions.loadPlayingPlayback(file) } returns recordId
                playbackRecord = PlaybackRecord(file, fcnpcNativeFunctions)
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
                every { fcnpcNativeFunctions.loadPlayingPlayback(file) } returns FCNPCConstants.INVALID_RECORD_ID
                caughtThrowable = catchThrowable { PlaybackRecord(file, fcnpcNativeFunctions) }
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
            every { fcnpcNativeFunctions.loadPlayingPlayback(any()) } returns recordId
            playbackRecord = PlaybackRecord("my_npc.rec", fcnpcNativeFunctions)
        }

        describe("id") {
            context("record is not destroyed") {
                it("should return record ID") {
                    assertThat(playbackRecord.id.value)
                            .isEqualTo(recordId)
                }
            }

            context("record is destroyed") {
                lateinit var caughtThrowable: Throwable

                beforeEach {
                    every { fcnpcNativeFunctions.unloadPlayingPlayback(any()) } returns true
                    playbackRecord.destroy()
                    caughtThrowable = catchThrowable { playbackRecord.id }
                }

                it("should throw exception") {
                    assertThat(caughtThrowable)
                            .isInstanceOf(AlreadyDestroyedException::class.java)
                }
            }
        }

        describe("destroy") {
            beforeEach {
                every { fcnpcNativeFunctions.unloadPlayingPlayback(any()) } returns true
                playbackRecord.destroy()
            }

            it("should call nativeFunctions.unloadPlayingPlayback") {
                verify { fcnpcNativeFunctions.unloadPlayingPlayback(recordId) }
            }
        }
    }
})