package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.OutputString
import ch.leadrian.samp.kamp.core.api.data.quaternionOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.FullyControllableNPCId
import ch.leadrian.samp.kamp.fcnpcwrapper.entity.id.PlaybackRecordId
import ch.leadrian.samp.kamp.fcnpcwrapper.exception.PlaybackFailedException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object PlaybackSpec : Spek({
    val fcnpcNativeFunctions by memoized { mockk<FCNPCNativeFunctions>() }
    val npcId = 123
    val npc by memoized {
        mockk<FullyControllableNPC> {
            every { id } returns FullyControllableNPCId.valueOf(npcId)
        }
    }
    val playback by memoized { Playback(npc, fcnpcNativeFunctions) }

    describe("start") {
        context("input is a file") {
            context("fcnpcNativeFunctions.startPlayingPlayback returns true") {
                beforeEach {
                    every {
                        fcnpcNativeFunctions.startPlayingPlayback(
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
                    playback.start(
                            "my_npc.rec", true, vector3DOf(1f, 2f, 3f),
                            quaternionOf(w = 4f, x = 5f, y = 6f, z = 7f)
                    )
                }

                it("should call fcnpcNativeFunctions.startPlayingPlayback") {
                    verify {
                        fcnpcNativeFunctions.startPlayingPlayback(
                                npcid = npcId,
                                file = "my_npc.rec",
                                recordid = FCNPCConstants.INVALID_RECORD_ID,
                                auto_unload = true,
                                delta_x = 1f,
                                delta_y = 2f,
                                delta_z = 3f,
                                delta_qw = 4f,
                                delta_qx = 5f,
                                delta_qy = 6f,
                                delta_qz = 7f
                        )
                    }
                }
            }

            context("fcnpcNativeFunctions.startPlayingPlayback returns false") {
                lateinit var caughtThrowable: Throwable

                beforeEach {
                    every {
                        fcnpcNativeFunctions.startPlayingPlayback(
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
                    } returns false
                    caughtThrowable = catchThrowable {
                        playback.start(
                                "my_npc.rec",
                                true,
                                vector3DOf(1f, 2f, 3f),
                                quaternionOf(w = 4f, x = 5f, y = 6f, z = 7f)
                        )
                    }
                }

                it("should throw exception") {
                    assertThat(caughtThrowable)
                            .isInstanceOf(PlaybackFailedException::class.java)
                            .hasMessage("Failed to start playback of file: my_npc.rec")
                }
            }

        }

        context("input is a PlaybackRecord") {
            val playbackRecordId = 69
            val playbackRecord by memoized {
                mockk<PlaybackRecord> {
                    every { id } returns PlaybackRecordId.valueOf(playbackRecordId)
                }
            }
            context("fcnpcNativeFunctions.startPlayingPlayback returns true") {
                beforeEach {
                    every {
                        fcnpcNativeFunctions.startPlayingPlayback(
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
                    playback.start(
                            playbackRecord, vector3DOf(1f, 2f, 3f),
                            quaternionOf(w = 4f, x = 5f, y = 6f, z = 7f)
                    )
                }

                it("should call fcnpcNativeFunctions.startPlayingPlayback") {
                    verify {
                        fcnpcNativeFunctions.startPlayingPlayback(
                                npcid = npcId,
                                file = "",
                                recordid = playbackRecordId,
                                auto_unload = false,
                                delta_x = 1f,
                                delta_y = 2f,
                                delta_z = 3f,
                                delta_qw = 4f,
                                delta_qx = 5f,
                                delta_qy = 6f,
                                delta_qz = 7f
                        )
                    }
                }
            }

            context("fcnpcNativeFunctions.startPlayingPlayback returns false") {
                lateinit var caughtThrowable: Throwable

                beforeEach {
                    every {
                        fcnpcNativeFunctions.startPlayingPlayback(
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
                    } returns false
                    caughtThrowable = catchThrowable {
                        playback.start(
                                playbackRecord, vector3DOf(1f, 2f, 3f),
                                quaternionOf(w = 4f, x = 5f, y = 6f, z = 7f)
                        )
                    }
                }

                it("should throw exception") {
                    assertThat(caughtThrowable)
                            .isInstanceOf(PlaybackFailedException::class.java)
                            .hasMessage("Failed to start playback of record with ID $playbackRecordId")
                }
            }

        }
    }

    describe("stop") {
        beforeEach {
            every { fcnpcNativeFunctions.stopPlayingPlayback(any()) } returns true
            playback.stop()
        }

        it("should call fcnpcNativeFunctions.stopPlayingPlayback") {
            verify { fcnpcNativeFunctions.stopPlayingPlayback(npcId) }
        }
    }

    describe("pause") {
        beforeEach {
            every { fcnpcNativeFunctions.pausePlayingPlayback(any()) } returns true
            playback.pause()
        }

        it("should call fcnpcNativeFunctions.pausePlayingPlayback") {
            verify { fcnpcNativeFunctions.pausePlayingPlayback(npcId) }
        }
    }

    describe("resume") {
        beforeEach {
            every { fcnpcNativeFunctions.resumePlayingPlayback(any()) } returns true
            playback.resume()
        }

        it("should call fcnpcNativeFunctions.resumePlayingPlayback") {
            verify { fcnpcNativeFunctions.resumePlayingPlayback(npcId) }
        }
    }

    describe("setFilePath") {
        val filePath = "my/npc/records"

        beforeEach {
            every { fcnpcNativeFunctions.setPlayingPlaybackPath(any(), any()) } returns true
            playback.setFilePath(filePath)
        }

        it("should call fcnpcNativeFunctions.setPlayingPlaybackPath") {
            verify { fcnpcNativeFunctions.setPlayingPlaybackPath(npcId, filePath) }
        }
    }

    describe("getFilePath") {
        val filePath = "my/npc/records"

        context("no size set") {
            beforeEach {
                every { fcnpcNativeFunctions.getPlayingPlaybackPath(npcId, any(), 512) } answers {
                    secondArg<OutputString>().value = filePath
                    true
                }
            }

            it("should return file path") {
                assertThat(playback.getFilePath())
                        .isEqualTo(filePath)
            }
        }

        context("size set") {
            beforeEach {
                every { fcnpcNativeFunctions.getPlayingPlaybackPath(npcId, any(), 256) } answers {
                    secondArg<OutputString>().value = filePath
                    true
                }
            }

            it("should return file path") {
                assertThat(playback.getFilePath(256))
                        .isEqualTo(filePath)
            }
        }
    }
})