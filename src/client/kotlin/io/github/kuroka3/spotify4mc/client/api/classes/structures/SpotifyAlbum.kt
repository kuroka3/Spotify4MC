package io.github.kuroka3.spotify4mc.client.api.classes.structures

import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyAlbum(
    val albumType: String,
    val totalTracks: Int,
    val availableMarkets: List<String>,
    val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val releaseDate: String,
    val releaseDatePrecision: String,
    val restrictions: Map<String, String>,
    val type: String,
    val uri: String,
    val artists: List<SpotifyArtist.SpotifySimplifiedArtist>,
    val tracks: SpotifyChildTrack,
    val copyrights: List<SpotifyCopyright>,
    val externalIds: Map<String, String>,
    val genres: List<String>,
    val label: String,
    val popularity: Int
) {

    companion object {
        fun fromJson(json: String): SpotifyAlbum {
            return JsonManager.gson.fromJson(json, SpotifyAlbum::class.java)
        }
    }

    val simplified: SpotifySimplifiedAlbum
        get() = SpotifySimplifiedAlbum(albumType, totalTracks, availableMarkets, externalUrls, href, id, images, name, releaseDate, releaseDatePrecision, restrictions, type, uri, artists)

    data class SpotifyChildTrack(
        val href: String,
        val limit: Int,
        val next: String?,
        val offset: Int,
        val previous: String?,
        val total: Int,
        val items: List<SpotifyTrack.SpotifySimplifiedTrack>
    )

    data class SpotifySimplifiedAlbum(
        val albumType: String,
        val totalTracks: Int,
        val availableMarkets: List<String>,
        val externalUrls: Map<String, String>,
        val href: String,
        val id: String,
        val images: List<SpotifyImage>,
        val name: String,
        val releaseDate: String,
        val releaseDatePrecision: String,
        val restrictions: Map<String, String>,
        val type: String,
        val uri: String,
        val artists: List<SpotifyArtist.SpotifySimplifiedArtist>
    )
}