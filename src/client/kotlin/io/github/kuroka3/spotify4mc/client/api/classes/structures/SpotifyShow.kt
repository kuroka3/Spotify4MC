package io.github.kuroka3.spotify4mc.client.api.classes.structures

import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyShow(
    val availableMarkets: List<String>,
    val copyrights: List<SpotifyCopyright>,
    val description: String,
    val htmlDescription: String,
    val explicit: Boolean,
    val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val isExternallyHosted: Boolean,
    val languages: List<String>,
    val mediaType: String,
    val name: String,
    val publisher: String,
    val type: String,
    val uri: String,
    val totalEpisodes: Int,
    val episodes: SpotifyChildEpisode
) {
    companion object {
        fun fromJson(json: String): SpotifyShow {
            return JsonManager.gson.fromJson(json, SpotifyShow::class.java)
        }
    }

    val simplified: SpotifySimplifiedShow
        get() = SpotifySimplifiedShow(availableMarkets, copyrights, description, htmlDescription, explicit, externalUrls, href, id, images, isExternallyHosted, languages, mediaType, name, publisher, type, uri, totalEpisodes)

    data class SpotifyChildEpisode(
        val href: String,
        val limit: Int,
        val next: String,
        val offset: Int,
        val previous: String,
        val total: Int,
        val items: List<SpotifyEpisode.SpotifySimplifiedEpisode>
    )

    data class SpotifySimplifiedShow(
        val availableMarkets: List<String>,
        val copyrights: List<SpotifyCopyright>,
        val description: String,
        val htmlDescription: String,
        val explicit: Boolean,
        val externalUrls: Map<String, String>,
        val href: String,
        val id: String,
        val images: List<SpotifyImage>,
        val isExternallyHosted: Boolean,
        val languages: List<String>,
        val mediaType: String,
        val name: String,
        val publisher: String,
        val type: String,
        val uri: String,
        val totalEpisodes: Int
    )
}