package io.github.kuroka3.spotify4mc.client.api.classes.structures

import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyImage(
    val url: String,
    val height: Int?,
    val width: Int?
) {
    companion object {
        fun fromJson(json: String): SpotifyImage {
            return JsonManager.gson.fromJson(json, SpotifyImage::class.java)
        }
    }
}