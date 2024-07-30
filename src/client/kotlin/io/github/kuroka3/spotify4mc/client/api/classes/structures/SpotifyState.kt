package io.github.kuroka3.spotify4mc.client.api.classes.structures

import com.mojang.datafixers.util.Either

data class SpotifyState(
    val repeatState: String,
    val shuffleState: Boolean,
    val context: SpotifyContext?,
    val timestamp: Int,
    val progressMs: Int,
    val isPlaying: Boolean,
    val item: Either<SpotifyTrack, SpotifyEpisode>,
    val currentlyPlayingType: String,
    val actions: Map<String, String>
) {
    data class SpotifyContext(
        val type: String,
        val href: String,
        val externalUrls: Map<String, String>,
        val uri: String
    )
}
