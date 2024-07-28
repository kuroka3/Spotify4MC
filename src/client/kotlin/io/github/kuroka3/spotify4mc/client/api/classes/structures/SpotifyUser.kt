package io.github.kuroka3.spotify4mc.client.api.classes.structures

data class SpotifyUser(
    val country: String,
    val displayName: String,
    val email: String,
    val explicitContent: Map<String, Boolean>,
    val externalUrls: Map<String, String>,
    val followers: SpotifyFollowers,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val product: String,
    val type: String,
    val uri: String,
) {

    val simplified: SpotifySimplifiedUser
        get() = SpotifySimplifiedUser(externalUrls, followers, href, id, type, uri, displayName)

    data class SpotifySimplifiedUser(
        val externalUrls: Map<String, String>,
        val followers: SpotifyFollowers,
        val href: String,
        val id: String,
        val type: String,
        val uri: String,
        val displayName: String?,
    )
}