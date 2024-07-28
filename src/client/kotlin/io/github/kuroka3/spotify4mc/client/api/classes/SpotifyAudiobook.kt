package io.github.kuroka3.spotify4mc.client.api.classes

data class SpotifyAudiobook(
    val authors: List<SpotifyAuthor>,
    val availableMarkets: List<String>,
    val copyrights: List<SpotifyCopyright>,
    val description: String,
    val htmlDescription: String,
    val edition: String,
    val explicit: Boolean,
    val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val languages: List<String>,
    val mediaType: String,
    val name: String,
    val narrators: List<SpotifyNarrator>,
    val publisher: String,
    val type: String,
    val uri: String,
    val totalChapters: Int,
    val chapters: SpotifyChildChapter
) {
    data class SpotifyChildChapter(
        val href: String,
        val limit: Int,
        val next: String?,
        val offset: Int,
        val previous: String?,
        val total: Int,
        val items: List<SpotifyChapter.SpotifySimplifiedChapter>
    )
}