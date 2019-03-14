package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.api.amx.OutputString
import ch.leadrian.samp.kamp.core.api.data.Quaternion
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.quaternionOf
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCConstants
import ch.leadrian.samp.kamp.fcnpcwrapper.FCNPCNativeFunctions
import ch.leadrian.samp.kamp.fcnpcwrapper.exception.PlaybackFailedException

class Playback
internal constructor(
        override val npc: FullyControllableNPC,
        private val nativeFunctions: FCNPCNativeFunctions
) : HasFullyControllableNPC {

    private companion object {

        val DEFAULT_PLAYBACK_QUATERNION = quaternionOf(0f, 0f, 0f, 0f)

    }

    @JvmOverloads
    fun start(
            file: String,
            autoUnload: Boolean = false,
            delta: Vector3D = Vector3D.ORIGIN,
            quaternion: Quaternion = DEFAULT_PLAYBACK_QUATERNION
    ) {
        val success = nativeFunctions.startPlayingPlayback(
                npcid = npc.id.value,
                file = file,
                recordid = FCNPCConstants.FCNPC_INVALID_RECORD_ID,
                auto_unload = autoUnload,
                delta_x = delta.x,
                delta_y = delta.y,
                delta_z = delta.z,
                delta_qw = quaternion.w,
                delta_qx = quaternion.x,
                delta_qy = quaternion.y,
                delta_qz = quaternion.z
        )
        if (!success) {
            throw PlaybackFailedException("Failed to start playback of file: $file")
        }
    }

    @JvmOverloads
    fun start(
            playbackRecord: PlaybackRecord,
            delta: Vector3D = Vector3D.ORIGIN,
            quaternion: Quaternion = DEFAULT_PLAYBACK_QUATERNION
    ) {
        val success = nativeFunctions.startPlayingPlayback(
                npcid = npc.id.value,
                file = "",
                recordid = playbackRecord.id.value,
                auto_unload = false,
                delta_x = delta.x,
                delta_y = delta.y,
                delta_z = delta.z,
                delta_qw = quaternion.w,
                delta_qx = quaternion.x,
                delta_qy = quaternion.y,
                delta_qz = quaternion.z
        )
        if (!success) {
            throw PlaybackFailedException("Failed to start playback of record with ID ${playbackRecord.id.value}")
        }
    }

    fun stop() {
        nativeFunctions.stopPlayingPlayback(npc.id.value)
    }

    fun pause() {
        nativeFunctions.pausePlayingPlayback(npc.id.value)
    }

    fun resume() {
        nativeFunctions.resumePlayingPlayback(npc.id.value)
    }

    fun setFilePath(file: String) {
        nativeFunctions.setPlayingPlaybackPath(npc.id.value, file)
    }

    @JvmOverloads
    fun getFilePath(size: Int = 512): String {
        val filePath = OutputString(size)
        nativeFunctions.getPlayingPlaybackPath(npc.id.value, filePath, size)
        return filePath.value
    }

}