package io.github.kuroka3.spotify4mc.client.api.classes.structures

import com.mojang.datafixers.util.Either
import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyPlaylist(
    val collaborative: Boolean,
    val description: String?,
    val externalUrls: Map<String, String>,
    val followers: SpotifyFollowers,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val owner: SpotifyUser,
    val public: Boolean,
    val snapshotId: String,
    val tracks: SpotifyChildTrack,
    val type: String,
    val uri: String
) {
    companion object {
        fun fromJson(json: String): SpotifyPlaylist {
            return JsonManager.gson.fromJson(json, SpotifyPlaylist::class.java)
        }
    }

    data class SpotifyChildTrack(
        val href: String,
        val limit: Int,
        val next: String?,
        val offset: Int,
        val previous: String?,
        val total: Int,
        val items: SpotifyPlaylistTrack,
    ) {
        data class SpotifyPlaylistTrack(
            val addedAt: String,
            val addedBy: SpotifyUser.SpotifySimplifiedUser,
            val isLocal: Boolean,
            val track: Either<SpotifyTrack, SpotifyEpisode>
        )
    }
}