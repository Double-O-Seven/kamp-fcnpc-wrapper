package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object PlaybackRecordIdSpec : Spek({
    describe("valueOf") {
        listOf(-1, 0, 999, 1000, FCNPCConstants.FCNPC_INVALID_RECORD_ID).forEach { value ->
            it("should return PlaybackRecordId with value $value") {
                val playbackRecordId = PlaybackRecordId.valueOf(value)

                assertThat(playbackRecordId.value)
                        .isEqualTo(value)
            }
        }

        listOf(0, 500, 999, FCNPCConstants.FCNPC_INVALID_RECORD_ID).forEach { value ->
            it("should used cached PlaybackRecordId for value $value") {
                val playbackRecordId = PlaybackRecordId.valueOf(value)

                assertThat(playbackRecordId)
                        .isSameAs(PlaybackRecordId.valueOf(value))
            }
        }

        listOf(-1, 1000, 1001).forEach { value ->
            it("should create new PlaybackRecordId for value $value") {
                val playbackRecordId = PlaybackRecordId.valueOf(value)

                assertThat(playbackRecordId)
                        .isNotSameAs(PlaybackRecordId.valueOf(value))
            }
        }
    }
})