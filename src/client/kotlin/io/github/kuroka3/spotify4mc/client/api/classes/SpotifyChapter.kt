package io.github.kuroka3.spotify4mc.client.api.classes

data class SpotifyChapter(
    val audioPreviewUrl: String?,
    val availableMarkets: List<String>,
    val chapterNumber: Int,
    val description: String,
    val htmlDescription: String,
    val durationMs: Int,
    val explicit: Boolean,
    val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val isPlayable: Boolean,
    val languages: List<String>,
    val name: String,
    val releaseDate: String,
    val releaseDatePrecision: String,
    // val resume_point
    val type: String,
    val uri: String,
    val restrictions: Map<String, String>,
    val audiobook: SpotifyAudiobook
) {

    val simplified: SpotifySimplifiedChapter
        get() = SpotifySimplifiedChapter(audioPreviewUrl, availableMarkets, chapterNumber, description, htmlDescription, durationMs, explicit, externalUrls, href, id, images, isPlayable, languages, name, releaseDate, releaseDatePrecision, type, uri, restrictions)

    data class SpotifySimplifiedChapter(
        val audioPreviewUrl: String?,
        val availableMarkets: List<String>,
        val chapterNumber: Int,
        val description: String,
        val htmlDescription: String,
        val durationMs: Int,
        val explicit: Boolean,
        val externalUrls: Map<String, String>,
        val href: String,
        val id: String,
        val images: List<SpotifyImage>,
        val isPlayable: Boolean,
        val languages: List<String>,
        val name: String,
        val releaseDate: String,
        val releaseDatePrecision: String,
        // val resume_point
        val type: String,
        val uri: String,
        val restrictions: Map<String, String>
    )
}