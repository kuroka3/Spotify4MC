package io.github.kuroka3.spotify4mc.client.api.classes.structures

data class SpotifyArtist(
    val externalUrls: Map<String, String>,
    val followers: Map<String, String>,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
) {

    val simplified: SpotifySimplifiedArtist
        get() = SpotifySimplifiedArtist(externalUrls, href, id, name, type, uri)

    data class SpotifySimplifiedArtist(
        val externalUrls: Map<String, String>,
        val href: String,
        val id: String,
        val name: String,
        val type: String,
        val uri: String
    )
}