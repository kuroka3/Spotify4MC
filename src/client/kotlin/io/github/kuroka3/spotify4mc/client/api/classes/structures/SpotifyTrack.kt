package io.github.kuroka3.spotify4mc.client.api.classes.structures

data class SpotifyTrack(
    val album: SpotifyAlbum.SpotifySimplifiedAlbum,
    val artists: List<SpotifyArtist.SpotifySimplifiedArtist>,
    val availableMarkets: List<String>,
    val discNumber: Int,
    val durationMs: Int,
    val explicit: Boolean,
    val externalIds: Map<String, String>,
    val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val isPlayable: Boolean,
    val linkedFrom: Map<String, String>,
    val restrictions: Map<String, String>,
    val name: String,
    val popularity: Int,
    val previewUrl: String?,
    val trackNumber: Int,
    val type: String,
    val uri: String,
    val isLocal: Boolean
) {

    val simplified: SpotifySimplifiedTrack
        get() = SpotifySimplifiedTrack(artists, availableMarkets, discNumber, durationMs, explicit, externalIds, href, id, isPlayable, linkedFrom, restrictions, name, previewUrl, trackNumber, type, uri, isLocal)

    data class SpotifySimplifiedTrack(
        val artists: List<SpotifyArtist.SpotifySimplifiedArtist>,
        val availableMarkets: List<String>,
        val discNumber: Int,
        val durationMs: Int,
        val explicit: Boolean,
        val externalIds: Map<String, String>,
        val href: String,
        val id: String,
        val isPlayable: Boolean,
        val linkedFrom: Map<String, String>,
        val restrictions: Map<String, String>,
        val name: String,
        val previewUrl: String?,
        val trackNumber: Int,
        val type: String,
        val uri: String,
        val isLocal: Boolean
    )
}