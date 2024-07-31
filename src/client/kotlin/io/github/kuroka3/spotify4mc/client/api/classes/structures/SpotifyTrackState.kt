package io.github.kuroka3.spotify4mc.client.api.classes.structures

import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyTrackState(
    val repeatState: String,
    val shuffleState: Boolean,
    val context: SpotifyContext?,
    val timestamp: Long,
    val progressMs: Int,
    val isPlaying: Boolean,
    val item: SpotifyTrack,
    val currentlyPlayingType: String
) {
    data class SpotifyContext(
        val type: String,
        val href: String,
        val externalUrls: Map<String, String>,
        val uri: String
    )

    companion object {
        fun fromJson(json: String): SpotifyTrackState {
            return JsonManager.gson.fromJson(json, SpotifyTrackState::class.java)
        }
    }
}
