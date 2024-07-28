package io.github.kuroka3.spotify4mc.client.api.classes

data class SpotifyEpisode(
    val audioPreviewUrl: String?,
    val description: String,
    val htmlDescription: String,
    val durationMs: Int,
    val explicit: Boolean,
    val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val isExternallyHosted: Boolean,
    val isPlayable: Boolean,
    val languages: List<String>,
    val name: String,
    val releaseDate: String,
    val releaseDatePrecision: String,
    // val resume_point
    val type: String,
    val uri: String,
    val restriction: Map<String, String>,
    val show: SpotifyShow.SpotifySimplifiedShow
) {

    val simplified: SpotifySimplifiedEpisode
        get() = SpotifySimplifiedEpisode(audioPreviewUrl, description, htmlDescription, durationMs, explicit, externalUrls, href, id, images, isExternallyHosted, isPlayable, languages, name, releaseDate, releaseDatePrecision, type, uri, restriction)

    data class SpotifySimplifiedEpisode(
        val audioPreviewUrl: String?,
        val description: String,
        val htmlDescription: String,
        val durationMs: Int,
        val explicit: Boolean,
        val externalUrls: Map<String, String>,
        val href: String,
        val id: String,
        val images: List<SpotifyImage>,
        val isExternallyHosted: Boolean,
        val isPlayable: Boolean,
        val languages: List<String>,
        val name: String,
        val releaseDate: String,
        val releaseDatePrecision: String,
        // val resume_point
        val type: String,
        val uri: String,
        val restriction: Map<String, String>
    )
}
